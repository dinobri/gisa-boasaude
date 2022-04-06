import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoExame, TipoExame } from '../tipo-exame.model';
import { TipoExameService } from '../service/tipo-exame.service';

@Component({
  selector: 'jhi-tipo-exame-update',
  templateUrl: './tipo-exame-update.component.html',
})
export class TipoExameUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
  });

  constructor(protected tipoExameService: TipoExameService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoExame }) => {
      this.updateForm(tipoExame);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoExame = this.createFromForm();
    if (tipoExame.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoExameService.update(tipoExame));
    } else {
      this.subscribeToSaveResponse(this.tipoExameService.create(tipoExame));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoExame>>): void {
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

  protected updateForm(tipoExame: ITipoExame): void {
    this.editForm.patchValue({
      id: tipoExame.id,
      nome: tipoExame.nome,
    });
  }

  protected createFromForm(): ITipoExame {
    return {
      ...new TipoExame(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
    };
  }
}
