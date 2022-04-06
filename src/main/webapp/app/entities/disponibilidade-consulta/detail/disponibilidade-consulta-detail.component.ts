import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDisponibilidadeConsulta } from '../disponibilidade-consulta.model';

@Component({
  selector: 'jhi-disponibilidade-consulta-detail',
  templateUrl: './disponibilidade-consulta-detail.component.html',
})
export class DisponibilidadeConsultaDetailComponent implements OnInit {
  disponibilidadeConsulta: IDisponibilidadeConsulta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disponibilidadeConsulta }) => {
      this.disponibilidadeConsulta = disponibilidadeConsulta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
