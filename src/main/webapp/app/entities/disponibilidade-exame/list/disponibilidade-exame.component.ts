import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDisponibilidadeExame } from '../disponibilidade-exame.model';
import { DisponibilidadeExameService } from '../service/disponibilidade-exame.service';
import { DisponibilidadeExameDeleteDialogComponent } from '../delete/disponibilidade-exame-delete-dialog.component';

@Component({
  selector: 'jhi-disponibilidade-exame',
  templateUrl: './disponibilidade-exame.component.html',
})
export class DisponibilidadeExameComponent implements OnInit {
  disponibilidadeExames?: IDisponibilidadeExame[];
  isLoading = false;

  constructor(protected disponibilidadeExameService: DisponibilidadeExameService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.disponibilidadeExameService.query().subscribe({
      next: (res: HttpResponse<IDisponibilidadeExame[]>) => {
        this.isLoading = false;
        this.disponibilidadeExames = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDisponibilidadeExame): number {
    return item.id!;
  }

  delete(disponibilidadeExame: IDisponibilidadeExame): void {
    const modalRef = this.modalService.open(DisponibilidadeExameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.disponibilidadeExame = disponibilidadeExame;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
