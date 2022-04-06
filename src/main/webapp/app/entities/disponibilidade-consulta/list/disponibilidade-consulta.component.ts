import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDisponibilidadeConsulta } from '../disponibilidade-consulta.model';
import { DisponibilidadeConsultaService } from '../service/disponibilidade-consulta.service';
import { DisponibilidadeConsultaDeleteDialogComponent } from '../delete/disponibilidade-consulta-delete-dialog.component';

@Component({
  selector: 'jhi-disponibilidade-consulta',
  templateUrl: './disponibilidade-consulta.component.html',
})
export class DisponibilidadeConsultaComponent implements OnInit {
  disponibilidadeConsultas?: IDisponibilidadeConsulta[];
  isLoading = false;

  constructor(protected disponibilidadeConsultaService: DisponibilidadeConsultaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.disponibilidadeConsultaService.query().subscribe({
      next: (res: HttpResponse<IDisponibilidadeConsulta[]>) => {
        this.isLoading = false;
        this.disponibilidadeConsultas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDisponibilidadeConsulta): number {
    return item.id!;
  }

  delete(disponibilidadeConsulta: IDisponibilidadeConsulta): void {
    const modalRef = this.modalService.open(DisponibilidadeConsultaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.disponibilidadeConsulta = disponibilidadeConsulta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
