import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExame } from '../exame.model';

@Component({
  selector: 'jhi-exame-detail',
  templateUrl: './exame-detail.component.html',
})
export class ExameDetailComponent implements OnInit {
  exame: IExame | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exame }) => {
      this.exame = exame;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
