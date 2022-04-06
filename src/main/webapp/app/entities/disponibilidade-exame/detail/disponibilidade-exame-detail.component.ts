import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDisponibilidadeExame } from '../disponibilidade-exame.model';

@Component({
  selector: 'jhi-disponibilidade-exame-detail',
  templateUrl: './disponibilidade-exame-detail.component.html',
})
export class DisponibilidadeExameDetailComponent implements OnInit {
  disponibilidadeExame: IDisponibilidadeExame | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disponibilidadeExame }) => {
      this.disponibilidadeExame = disponibilidadeExame;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
