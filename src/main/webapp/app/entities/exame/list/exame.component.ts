import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExame } from '../exame.model';
import { ExameService } from '../service/exame.service';
import { ExameDeleteDialogComponent } from '../delete/exame-delete-dialog.component';

@Component({
  selector: 'jhi-exame',
  templateUrl: './exame.component.html',
})
export class ExameComponent implements OnInit {
  exames?: IExame[];
  isLoading = false;

  constructor(protected exameService: ExameService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.exameService.query().subscribe({
      next: (res: HttpResponse<IExame[]>) => {
        this.isLoading = false;
        this.exames = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IExame): number {
    return item.id!;
  }

  delete(exame: IExame): void {
    const modalRef = this.modalService.open(ExameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.exame = exame;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
