import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoExame } from '../tipo-exame.model';
import { TipoExameService } from '../service/tipo-exame.service';
import { TipoExameDeleteDialogComponent } from '../delete/tipo-exame-delete-dialog.component';

@Component({
  selector: 'jhi-tipo-exame',
  templateUrl: './tipo-exame.component.html',
})
export class TipoExameComponent implements OnInit {
  tipoExames?: ITipoExame[];
  isLoading = false;

  constructor(protected tipoExameService: TipoExameService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tipoExameService.query().subscribe({
      next: (res: HttpResponse<ITipoExame[]>) => {
        this.isLoading = false;
        this.tipoExames = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITipoExame): number {
    return item.id!;
  }

  delete(tipoExame: ITipoExame): void {
    const modalRef = this.modalService.open(TipoExameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoExame = tipoExame;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
