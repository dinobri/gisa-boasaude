import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPlano, Plano } from '../plano.model';
import { PlanoService } from '../service/plano.service';
import { CategoriaPlano } from 'app/entities/enumerations/categoria-plano.model';
import { TipoPlano } from 'app/entities/enumerations/tipo-plano.model';

@Component({
  selector: 'jhi-plano-update',
  templateUrl: './plano-update.component.html',
})
export class PlanoUpdateComponent implements OnInit {
  isSaving = false;
  categoriaPlanoValues = Object.keys(CategoriaPlano);
  tipoPlanoValues = Object.keys(TipoPlano);

  editForm = this.fb.group({
    id: [],
    nome: [],
    codigoANS: [],
    categoria: [],
    tipo: [],
    odonto: [],
    idadeMin: [],
    idadeMax: [],
    quantidadeConsultasAno: [],
    quanatidadeExamesAno: [],
    valorMensalidade: [],
  });

  constructor(protected planoService: PlanoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plano }) => {
      this.updateForm(plano);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plano = this.createFromForm();
    if (plano.id !== undefined) {
      this.subscribeToSaveResponse(this.planoService.update(plano));
    } else {
      this.subscribeToSaveResponse(this.planoService.create(plano));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlano>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(plano: IPlano): void {
    this.editForm.patchValue({
      id: plano.id,
      nome: plano.nome,
      codigoANS: plano.codigoANS,
      categoria: plano.categoria,
      tipo: plano.tipo,
      odonto: plano.odonto,
      idadeMin: plano.idadeMin,
      idadeMax: plano.idadeMax,
      quantidadeConsultasAno: plano.quantidadeConsultasAno,
      quanatidadeExamesAno: plano.quanatidadeExamesAno,
      valorMensalidade: plano.valorMensalidade,
    });
  }

  protected createFromForm(): IPlano {
    return {
      ...new Plano(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      codigoANS: this.editForm.get(['codigoANS'])!.value,
      categoria: this.editForm.get(['categoria'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      odonto: this.editForm.get(['odonto'])!.value,
      idadeMin: this.editForm.get(['idadeMin'])!.value,
      idadeMax: this.editForm.get(['idadeMax'])!.value,
      quantidadeConsultasAno: this.editForm.get(['quantidadeConsultasAno'])!.value,
      quanatidadeExamesAno: this.editForm.get(['quanatidadeExamesAno'])!.value,
      valorMensalidade: this.editForm.get(['valorMensalidade'])!.value,
    };
  }
}
