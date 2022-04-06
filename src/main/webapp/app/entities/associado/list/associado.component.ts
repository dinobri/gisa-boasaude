import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssociado } from '../associado.model';
import { AssociadoService } from '../service/associado.service';
import { AssociadoDeleteDialogComponent } from '../delete/associado-delete-dialog.component';

@Component({
  selector: 'jhi-associado',
  templateUrl: './associado.component.html',
})
export class AssociadoComponent implements OnInit {
  associados?: IAssociado[];
  isLoading = false;

  constructor(protected associadoService: AssociadoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.associadoService.query().subscribe({
      next: (res: HttpResponse<IAssociado[]>) => {
        this.isLoading = false;
        this.associados = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAssociado): number {
    return item.id!;
  }

  delete(associado: IAssociado): void {
    const modalRef = this.modalService.open(AssociadoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.associado = associado;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
