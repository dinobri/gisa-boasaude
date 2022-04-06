import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConveniado } from '../conveniado.model';
import { ConveniadoService } from '../service/conveniado.service';
import { ConveniadoDeleteDialogComponent } from '../delete/conveniado-delete-dialog.component';

@Component({
  selector: 'jhi-conveniado',
  templateUrl: './conveniado.component.html',
})
export class ConveniadoComponent implements OnInit {
  conveniados?: IConveniado[];
  isLoading = false;

  constructor(protected conveniadoService: ConveniadoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.conveniadoService.query().subscribe({
      next: (res: HttpResponse<IConveniado[]>) => {
        this.isLoading = false;
        this.conveniados = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IConveniado): number {
    return item.id!;
  }

  delete(conveniado: IConveniado): void {
    const modalRef = this.modalService.open(ConveniadoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.conveniado = conveniado;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
