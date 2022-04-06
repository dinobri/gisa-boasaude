import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDisponibilidadeExame, DisponibilidadeExame } from '../disponibilidade-exame.model';
import { DisponibilidadeExameService } from '../service/disponibilidade-exame.service';
import { IPrestador } from 'app/entities/prestador/prestador.model';
import { PrestadorService } from 'app/entities/prestador/service/prestador.service';
import { ITipoExame } from 'app/entities/tipo-exame/tipo-exame.model';
import { TipoExameService } from 'app/entities/tipo-exame/service/tipo-exame.service';

@Component({
  selector: 'jhi-disponibilidade-exame-update',
  templateUrl: './disponibilidade-exame-update.component.html',
})
export class DisponibilidadeExameUpdateComponent implements OnInit {
  isSaving = false;

  prestadorsSharedCollection: IPrestador[] = [];
  tipoExamesSharedCollection: ITipoExame[] = [];

  editForm = this.fb.group({
    id: [],
    horaInicio: [],
    horaFim: [],
    prestador: [],
    tipoExame: [],
  });

  constructor(
    protected disponibilidadeExameService: DisponibilidadeExameService,
    protected prestadorService: PrestadorService,
    protected tipoExameService: TipoExameService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disponibilidadeExame }) => {
      if (disponibilidadeExame.id === undefined) {
        const today = dayjs().startOf('day');
        disponibilidadeExame.horaInicio = today;
        disponibilidadeExame.horaFim = today;
      }

      this.updateForm(disponibilidadeExame);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disponibilidadeExame = this.createFromForm();
    if (disponibilidadeExame.id !== undefined) {
      this.subscribeToSaveResponse(this.disponibilidadeExameService.update(disponibilidadeExame));
    } else {
      this.subscribeToSaveResponse(this.disponibilidadeExameService.create(disponibilidadeExame));
    }
  }

  trackPrestadorById(_index: number, item: IPrestador): number {
    return item.id!;
  }

  trackTipoExameById(_index: number, item: ITipoExame): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisponibilidadeExame>>): void {
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

  protected updateForm(disponibilidadeExame: IDisponibilidadeExame): void {
    this.editForm.patchValue({
      id: disponibilidadeExame.id,
      horaInicio: disponibilidadeExame.horaInicio ? disponibilidadeExame.horaInicio.format(DATE_TIME_FORMAT) : null,
      horaFim: disponibilidadeExame.horaFim ? disponibilidadeExame.horaFim.format(DATE_TIME_FORMAT) : null,
      prestador: disponibilidadeExame.prestador,
      tipoExame: disponibilidadeExame.tipoExame,
    });

    this.prestadorsSharedCollection = this.prestadorService.addPrestadorToCollectionIfMissing(
      this.prestadorsSharedCollection,
      disponibilidadeExame.prestador
    );
    this.tipoExamesSharedCollection = this.tipoExameService.addTipoExameToCollectionIfMissing(
      this.tipoExamesSharedCollection,
      disponibilidadeExame.tipoExame
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

    this.tipoExameService
      .query()
      .pipe(map((res: HttpResponse<ITipoExame[]>) => res.body ?? []))
      .pipe(
        map((tipoExames: ITipoExame[]) =>
          this.tipoExameService.addTipoExameToCollectionIfMissing(tipoExames, this.editForm.get('tipoExame')!.value)
        )
      )
      .subscribe((tipoExames: ITipoExame[]) => (this.tipoExamesSharedCollection = tipoExames));
  }

  protected createFromForm(): IDisponibilidadeExame {
    return {
      ...new DisponibilidadeExame(),
      id: this.editForm.get(['id'])!.value,
      horaInicio: this.editForm.get(['horaInicio'])!.value ? dayjs(this.editForm.get(['horaInicio'])!.value, DATE_TIME_FORMAT) : undefined,
      horaFim: this.editForm.get(['horaFim'])!.value ? dayjs(this.editForm.get(['horaFim'])!.value, DATE_TIME_FORMAT) : undefined,
      prestador: this.editForm.get(['prestador'])!.value,
      tipoExame: this.editForm.get(['tipoExame'])!.value,
    };
  }
}
