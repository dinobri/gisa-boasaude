import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDisponibilidadeConsulta, DisponibilidadeConsulta } from '../disponibilidade-consulta.model';
import { DisponibilidadeConsultaService } from '../service/disponibilidade-consulta.service';
import { IPrestador } from 'app/entities/prestador/prestador.model';
import { PrestadorService } from 'app/entities/prestador/service/prestador.service';

@Component({
  selector: 'jhi-disponibilidade-consulta-update',
  templateUrl: './disponibilidade-consulta-update.component.html',
})
export class DisponibilidadeConsultaUpdateComponent implements OnInit {
  isSaving = false;

  prestadorsSharedCollection: IPrestador[] = [];

  editForm = this.fb.group({
    id: [],
    horaInicio: [],
    horaFim: [],
    prestador: [],
  });

  constructor(
    protected disponibilidadeConsultaService: DisponibilidadeConsultaService,
    protected prestadorService: PrestadorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disponibilidadeConsulta }) => {
      if (disponibilidadeConsulta.id === undefined) {
        const today = dayjs().startOf('day');
        disponibilidadeConsulta.horaInicio = today;
        disponibilidadeConsulta.horaFim = today;
      }

      this.updateForm(disponibilidadeConsulta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disponibilidadeConsulta = this.createFromForm();
    if (disponibilidadeConsulta.id !== undefined) {
      this.subscribeToSaveResponse(this.disponibilidadeConsultaService.update(disponibilidadeConsulta));
    } else {
      this.subscribeToSaveResponse(this.disponibilidadeConsultaService.create(disponibilidadeConsulta));
    }
  }

  trackPrestadorById(_index: number, item: IPrestador): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisponibilidadeConsulta>>): void {
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

  protected updateForm(disponibilidadeConsulta: IDisponibilidadeConsulta): void {
    this.editForm.patchValue({
      id: disponibilidadeConsulta.id,
      horaInicio: disponibilidadeConsulta.horaInicio ? disponibilidadeConsulta.horaInicio.format(DATE_TIME_FORMAT) : null,
      horaFim: disponibilidadeConsulta.horaFim ? disponibilidadeConsulta.horaFim.format(DATE_TIME_FORMAT) : null,
      prestador: disponibilidadeConsulta.prestador,
    });

    this.prestadorsSharedCollection = this.prestadorService.addPrestadorToCollectionIfMissing(
      this.prestadorsSharedCollection,
      disponibilidadeConsulta.prestador
    );
  }

  protected loadRelationshipsOptions(): void {
    this.prestadorService
      .query()
      .pipe(map((res: HttpResponse<IPrestador[]>) => res.body ?? []))
      .pipe(
        map((prestadors: IPrestador[]) =>
          this.prestadorService.addPrestadorToCollectionIfMissing(prestadors, this.editForm.get('prestador')!.value)
        )
      )
      .subscribe((prestadors: IPrestador[]) => (this.prestadorsSharedCollection = prestadors));
  }

  protected createFromForm(): IDisponibilidadeConsulta {
    return {
      ...new DisponibilidadeConsulta(),
      id: this.editForm.get(['id'])!.value,
      horaInicio: this.editForm.get(['horaInicio'])!.value ? dayjs(this.editForm.get(['horaInicio'])!.value, DATE_TIME_FORMAT) : undefined,
      horaFim: this.editForm.get(['horaFim'])!.value ? dayjs(this.editForm.get(['horaFim'])!.value, DATE_TIME_FORMAT) : undefined,
      prestador: this.editForm.get(['prestador'])!.value,
    };
  }
}
