import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrestador } from '../prestador.model';
import { PrestadorService } from '../service/prestador.service';
import { PrestadorDeleteDialogComponent } from '../delete/prestador-delete-dialog.component';

@Component({
  selector: 'jhi-prestador',
  templateUrl: './prestador.component.html',
})
export class PrestadorComponent implements OnInit {
  prestadors?: IPrestador[];
  isLoading = false;

  constructor(protected prestadorService: PrestadorService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.prestadorService.query().subscribe({
      next: (res: HttpResponse<IPrestador[]>) => {
        this.isLoading = false;
        this.prestadors = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPrestador): number {
    return item.id!;
  }

  delete(prestador: IPrestador): void {
    const modalRef = this.modalService.open(PrestadorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.prestador = prestador;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
