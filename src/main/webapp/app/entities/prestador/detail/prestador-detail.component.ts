import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrestador } from '../prestador.model';

@Component({
  selector: 'jhi-prestador-detail',
  templateUrl: './prestador-detail.component.html',
})
export class PrestadorDetailComponent implements OnInit {
  prestador: IPrestador | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestador }) => {
      this.prestador = prestador;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
