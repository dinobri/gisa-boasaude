import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEspecialidade, Especialidade } from '../especialidade.model';
import { EspecialidadeService } from '../service/especialidade.service';

@Component({
  selector: 'jhi-especialidade-update',
  templateUrl: './especialidade-update.component.html',
})
export class EspecialidadeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
  });

  constructor(protected especialidadeService: EspecialidadeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ especialidade }) => {
      this.updateForm(especialidade);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const especialidade = this.createFromForm();
    if (especialidade.id !== undefined) {
      this.subscribeToSaveResponse(this.especialidadeService.update(especialidade));
    } else {
      this.subscribeToSaveResponse(this.especialidadeService.create(especialidade));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEspecialidade>>): void {
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

  protected updateForm(especialidade: IEspecialidade): void {
    this.editForm.patchValue({
      id: especialidade.id,
      nome: especialidade.nome,
    });
  }

  protected createFromForm(): IEspecialidade {
    return {
      ...new Especialidade(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
    };
  }
}
