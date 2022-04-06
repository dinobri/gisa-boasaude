import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IExame, Exame } from '../exame.model';
import { ExameService } from '../service/exame.service';
import { IDisponibilidadeExame } from 'app/entities/disponibilidade-exame/disponibilidade-exame.model';
import { DisponibilidadeExameService } from 'app/entities/disponibilidade-exame/service/disponibilidade-exame.service';
import { IAssociado } from 'app/entities/associado/associado.model';
import { AssociadoService } from 'app/entities/associado/service/associado.service';
import { SituacaoAtendimento } from 'app/entities/enumerations/situacao-atendimento.model';

@Component({
  selector: 'jhi-exame-update',
  templateUrl: './exame-update.component.html',
})
export class ExameUpdateComponent implements OnInit {
  isSaving = false;
  situacaoAtendimentoValues = Object.keys(SituacaoAtendimento);

  disponibilidadeExamesCollection: IDisponibilidadeExame[] = [];
  associadosSharedCollection: IAssociado[] = [];

  editForm = this.fb.group({
    id: [],
    situacao: [],
    disponibilidadeExame: [],
    associado: [],
  });

  constructor(
    protected exameService: ExameService,
    protected disponibilidadeExameService: DisponibilidadeExameService,
    protected associadoService: AssociadoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exame }) => {
      this.updateForm(exame);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exame = this.createFromForm();
    if (exame.id !== undefined) {
      this.subscribeToSaveResponse(this.exameService.update(exame));
    } else {
      this.subscribeToSaveResponse(this.exameService.create(exame));
    }
  }

  trackDisponibilidadeExameById(_index: number, item: IDisponibilidadeExame): number {
    return item.id!;
  }

  trackAssociadoById(_index: number, item: IAssociado): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExame>>): void {
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

  protected updateForm(exame: IExame): void {
    this.editForm.patchValue({
      id: exame.id,
      situacao: exame.situacao,
      disponibilidadeExame: exame.disponibilidadeExame,
      associado: exame.associado,
    });

    this.disponibilidadeExamesCollection = this.disponibilidadeExameService.addDisponibilidadeExameToCollectionIfMissing(
      this.disponibilidadeExamesCollection,
      exame.disponibilidadeExame
    );
    this.associadosSharedCollection = this.associadoService.addAssociadoToCollectionIfMissing(
      this.associadosSharedCollection,
      exame.associado
    );
  }

  protected loadRelationshipsOptions(): void {
    this.disponibilidadeExameService
      .query({ filter: 'exame-is-null' })
      .pipe(map((res: HttpResponse<IDisponibilidadeExame[]>) => res.body ?? []))
      .pipe(
        map((disponibilidadeExames: IDisponibilidadeExame[]) =>
          this.disponibilidadeExameService.addDisponibilidadeExameToCollectionIfMissing(
            disponibilidadeExames,
            this.editForm.get('disponibilidadeExame')!.value
          )
        )
      )
      .subscribe((disponibilidadeExames: IDisponibilidadeExame[]) => (this.disponibilidadeExamesCollection = disponibilidadeExames));

    this.associadoService
      .query()
      .pipe(map((res: HttpResponse<IAssociado[]>) => res.body ?? []))
      .pipe(
        map((associados: IAssociado[]) =>
          this.associadoService.addAssociadoToCollectionIfMissing(associados, this.editForm.get('associado')!.value)
        )
      )
      .subscribe((associados: IAssociado[]) => (this.associadosSharedCollection = associados));
  }

  protected createFromForm(): IExame {
    return {
      ...new Exame(),
      id: this.editForm.get(['id'])!.value,
      situacao: this.editForm.get(['situacao'])!.value,
      disponibilidadeExame: this.editForm.get(['disponibilidadeExame'])!.value,
      associado: this.editForm.get(['associado'])!.value,
    };
  }
}
