import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlano } from '../plano.model';
import { PlanoService } from '../service/plano.service';
import { PlanoDeleteDialogComponent } from '../delete/plano-delete-dialog.component';

@Component({
  selector: 'jhi-plano',
  templateUrl: './plano.component.html',
})
export class PlanoComponent implements OnInit {
  planos?: IPlano[];
  isLoading = false;

  constructor(protected planoService: PlanoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.planoService.query().subscribe({
      next: (res: HttpResponse<IPlano[]>) => {
        this.isLoading = false;
        this.planos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPlano): number {
    return item.id!;
  }

  delete(plano: IPlano): void {
    const modalRef = this.modalService.open(PlanoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plano = plano;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
