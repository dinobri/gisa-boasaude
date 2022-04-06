import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IConsulta, Consulta } from '../consulta.model';
import { ConsultaService } from '../service/consulta.service';
import { IDisponibilidadeConsulta } from 'app/entities/disponibilidade-consulta/disponibilidade-consulta.model';
import { DisponibilidadeConsultaService } from 'app/entities/disponibilidade-consulta/service/disponibilidade-consulta.service';
import { IAssociado } from 'app/entities/associado/associado.model';
import { AssociadoService } from 'app/entities/associado/service/associado.service';
import { SituacaoAtendimento } from 'app/entities/enumerations/situacao-atendimento.model';

@Component({
  selector: 'jhi-consulta-update',
  templateUrl: './consulta-update.component.html',
})
export class ConsultaUpdateComponent implements OnInit {
  isSaving = false;
  situacaoAtendimentoValues = Object.keys(SituacaoAtendimento);

  disponibilidadeConsultasCollection: IDisponibilidadeConsulta[] = [];
  associadosSharedCollection: IAssociado[] = [];

  editForm = this.fb.group({
    id: [],
    situacao: [],
    disponibilidadeConsulta: [],
    associado: [],
  });

  constructor(
    protected consultaService: ConsultaService,
    protected disponibilidadeConsultaService: DisponibilidadeConsultaService,
    protected associadoService: AssociadoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consulta }) => {
      this.updateForm(consulta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consulta = this.createFromForm();
    if (consulta.id !== undefined) {
      this.subscribeToSaveResponse(this.consultaService.update(consulta));
    } else {
      this.subscribeToSaveResponse(this.consultaService.create(consulta));
    }
  }

  trackDisponibilidadeConsultaById(_index: number, item: IDisponibilidadeConsulta): number {
    return item.id!;
  }

  trackAssociadoById(_index: number, item: IAssociado): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsulta>>): void {
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

  protected updateForm(consulta: IConsulta): void {
    this.editForm.patchValue({
      id: consulta.id,
      situacao: consulta.situacao,
      disponibilidadeConsulta: consulta.disponibilidadeConsulta,
      associado: consulta.associado,
    });

    this.disponibilidadeConsultasCollection = this.disponibilidadeConsultaService.addDisponibilidadeConsultaToCollectionIfMissing(
      this.disponibilidadeConsultasCollection,
      consulta.disponibilidadeConsulta
    );
    this.associadosSharedCollection = this.associadoService.addAssociadoToCollectionIfMissing(
      this.associadosSharedCollection,
      consulta.associado
    );
  }

  protected loadRelationshipsOptions(): void {
    this.disponibilidadeConsultaService
      .query({ filter: 'consulta-is-null' })
      .pipe(map((res: HttpResponse<IDisponibilidadeConsulta[]>) => res.body ?? []))
      .pipe(
        map((disponibilidadeConsultas: IDisponibilidadeConsulta[]) =>
          this.disponibilidadeConsultaService.addDisponibilidadeConsultaToCollectionIfMissing(
            disponibilidadeConsultas,
            this.editForm.get('disponibilidadeConsulta')!.value
          )
        )
      )
      .subscribe(
        (disponibilidadeConsultas: IDisponibilidadeConsulta[]) => (this.disponibilidadeConsultasCollection = disponibilidadeConsultas)
      );

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

  protected createFromForm(): IConsulta {
    return {
      ...new Consulta(),
      id: this.editForm.get(['id'])!.value,
      situacao: this.editForm.get(['situacao'])!.value,
      disponibilidadeConsulta: this.editForm.get(['disponibilidadeConsulta'])!.value,
      associado: this.editForm.get(['associado'])!.value,
    };
  }
}
