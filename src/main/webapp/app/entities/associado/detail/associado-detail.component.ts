import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssociado } from '../associado.model';

@Component({
  selector: 'jhi-associado-detail',
  templateUrl: './associado-detail.component.html',
})
export class AssociadoDetailComponent implements OnInit {
  associado: IAssociado | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ associado }) => {
      this.associado = associado;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
