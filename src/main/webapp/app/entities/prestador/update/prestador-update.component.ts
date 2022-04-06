import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPrestador, Prestador } from '../prestador.model';
import { PrestadorService } from '../service/prestador.service';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { IEspecialidade } from 'app/entities/especialidade/especialidade.model';
import { EspecialidadeService } from 'app/entities/especialidade/service/especialidade.service';
import { Sexo } from 'app/entities/enumerations/sexo.model';

@Component({
  selector: 'jhi-prestador-update',
  templateUrl: './prestador-update.component.html',
})
export class PrestadorUpdateComponent implements OnInit {
  isSaving = false;
  sexoValues = Object.keys(Sexo);

  enderecoAtendimentosCollection: IEndereco[] = [];
  especialidadesSharedCollection: IEspecialidade[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    sobrenome: [],
    dataNascimento: [],
    sexo: [],
    numeroRegistroProfissional: [],
    telefone: [],
    enderecoAtendimento: [],
    especialidade: [],
  });

  constructor(
    protected prestadorService: PrestadorService,
    protected enderecoService: EnderecoService,
    protected especialidadeService: EspecialidadeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestador }) => {
      if (prestador.id === undefined) {
        const today = dayjs().startOf('day');
        prestador.dataNascimento = today;
      }

      this.updateForm(prestador);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prestador = this.createFromForm();
    if (prestador.id !== undefined) {
      this.subscribeToSaveResponse(this.prestadorService.update(prestador));
    } else {
      this.subscribeToSaveResponse(this.prestadorService.create(prestador));
    }
  }

  trackEnderecoById(_index: number, item: IEndereco): number {
    return item.id!;
  }

  trackEspecialidadeById(_index: number, item: IEspecialidade): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrestador>>): void {
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

  protected updateForm(prestador: IPrestador): void {
    this.editForm.patchValue({
      id: prestador.id,
      nome: prestador.nome,
      sobrenome: prestador.sobrenome,
      dataNascimento: prestador.dataNascimento ? prestador.dataNascimento.format(DATE_TIME_FORMAT) : null,
      sexo: prestador.sexo,
      numeroRegistroProfissional: prestador.numeroRegistroProfissional,
      telefone: prestador.telefone,
      enderecoAtendimento: prestador.enderecoAtendimento,
      especialidade: prestador.especialidade,
    });

    this.enderecoAtendimentosCollection = this.enderecoService.addEnderecoToCollectionIfMissing(
      this.enderecoAtendimentosCollection,
      prestador.enderecoAtendimento
    );
    this.especialidadesSharedCollection = this.especialidadeService.addEspecialidadeToCollectionIfMissing(
      this.especialidadesSharedCollection,
      prestador.especialidade
    );
  }

  protected loadRelationshipsOptions(): void {
    this.enderecoService
      .query({ filter: 'prestador-is-null' })
      .pipe(map((res: HttpResponse<IEndereco[]>) => res.body ?? []))
      .pipe(
        map((enderecos: IEndereco[]) =>
          this.enderecoService.addEnderecoToCollectionIfMissing(enderecos, this.editForm.get('enderecoAtendimento')!.value)
        )
      )
      .subscribe((enderecos: IEndereco[]) => (this.enderecoAtendimentosCollection = enderecos));

    this.especialidadeService
      .query()
      .pipe(map((res: HttpResponse<IEspecialidade[]>) => res.body ?? []))
      .pipe(
        map((especialidades: IEspecialidade[]) =>
          this.especialidadeService.addEspecialidadeToCollectionIfMissing(especialidades, this.editForm.get('especialidade')!.value)
        )
      )
      .subscribe((especialidades: IEspecialidade[]) => (this.especialidadesSharedCollection = especialidades));
  }

  protected createFromForm(): IPrestador {
    return {
      ...new Prestador(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      sobrenome: this.editForm.get(['sobrenome'])!.value,
      dataNascimento: this.editForm.get(['dataNascimento'])!.value
        ? dayjs(this.editForm.get(['dataNascimento'])!.value, DATE_TIME_FORMAT)
        : undefined,
      sexo: this.editForm.get(['sexo'])!.value,
      numeroRegistroProfissional: this.editForm.get(['numeroRegistroProfissional'])!.value,
      telefone: this.editForm.get(['telefone'])!.value,
      enderecoAtendimento: this.editForm.get(['enderecoAtendimento'])!.value,
      especialidade: this.editForm.get(['especialidade'])!.value,
    };
  }
}
