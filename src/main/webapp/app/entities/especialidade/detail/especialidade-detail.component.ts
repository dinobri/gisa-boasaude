import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEspecialidade } from '../especialidade.model';

@Component({
  selector: 'jhi-especialidade-detail',
  templateUrl: './especialidade-detail.component.html',
})
export class EspecialidadeDetailComponent implements OnInit {
  especialidade: IEspecialidade | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ especialidade }) => {
      this.especialidade = especialidade;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
