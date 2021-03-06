import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEndereco, Endereco } from '../endereco.model';
import { EnderecoService } from '../service/endereco.service';
import { UF } from 'app/entities/enumerations/uf.model';

@Component({
  selector: 'jhi-endereco-update',
  templateUrl: './endereco-update.component.html',
})
export class EnderecoUpdateComponent implements OnInit {
  isSaving = false;
  uFValues = Object.keys(UF);

  editForm = this.fb.group({
    id: [],
    cep: [],
    logradouro: [],
    numero: [],
    cidade: [],
    uf: [],
    bairro: [],
    complemento: [],
  });

  constructor(protected enderecoService: EnderecoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ endereco }) => {
      this.updateForm(endereco);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const endereco = this.createFromForm();
    if (endereco.id !== undefined) {
      this.subscribeToSaveResponse(this.enderecoService.update(endereco));
    } else {
      this.subscribeToSaveResponse(this.enderecoService.create(endereco));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEndereco>>): void {
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

  protected updateForm(endereco: IEndereco): void {
    this.editForm.patchValue({
      id: endereco.id,
      cep: endereco.cep,
      logradouro: endereco.logradouro,
      numero: endereco.numero,
      cidade: endereco.cidade,
      uf: endereco.uf,
      bairro: endereco.bairro,
      complemento: endereco.complemento,
    });
  }

  protected createFromForm(): IEndereco {
    return {
      ...new Endereco(),
      id: this.editForm.get(['id'])!.value,
      cep: this.editForm.get(['cep'])!.value,
      logradouro: this.editForm.get(['logradouro'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      cidade: this.editForm.get(['cidade'])!.value,
      uf: this.editForm.get(['uf'])!.value,
      bairro: this.editForm.get(['bairro'])!.value,
      complemento: this.editForm.get(['complemento'])!.value,
    };
  }
}
