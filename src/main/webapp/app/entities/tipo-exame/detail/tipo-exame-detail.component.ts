import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoExame } from '../tipo-exame.model';

@Component({
  selector: 'jhi-tipo-exame-detail',
  templateUrl: './tipo-exame-detail.component.html',
})
export class TipoExameDetailComponent implements OnInit {
  tipoExame: ITipoExame | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoExame }) => {
      this.tipoExame = tipoExame;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
