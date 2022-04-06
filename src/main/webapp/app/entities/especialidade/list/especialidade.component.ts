import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEspecialidade } from '../especialidade.model';
import { EspecialidadeService } from '../service/especialidade.service';
import { EspecialidadeDeleteDialogComponent } from '../delete/especialidade-delete-dialog.component';

@Component({
  selector: 'jhi-especialidade',
  templateUrl: './especialidade.component.html',
})
export class EspecialidadeComponent implements OnInit {
  especialidades?: IEspecialidade[];
  isLoading = false;

  constructor(protected especialidadeService: EspecialidadeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.especialidadeService.query().subscribe({
      next: (res: HttpResponse<IEspecialidade[]>) => {
        this.isLoading = false;
        this.especialidades = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IEspecialidade): number {
    return item.id!;
  }

  delete(especialidade: IEspecialidade): void {
    const modalRef = this.modalService.open(EspecialidadeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.especialidade = especialidade;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
