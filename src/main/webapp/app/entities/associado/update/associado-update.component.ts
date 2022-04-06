import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAssociado, Associado } from '../associado.model';
import { AssociadoService } from '../service/associado.service';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { IPlano } from 'app/entities/plano/plano.model';
import { PlanoService } from 'app/entities/plano/service/plano.service';
import { EstadoCivil } from 'app/entities/enumerations/estado-civil.model';
import { Sexo } from 'app/entities/enumerations/sexo.model';
import { UF } from 'app/entities/enumerations/uf.model';
import { SituacaoAssociado } from 'app/entities/enumerations/situacao-associado.model';

@Component({
  selector: 'jhi-associado-update',
  templateUrl: './associado-update.component.html',
})
export class AssociadoUpdateComponent implements OnInit {
  isSaving = false;
  estadoCivilValues = Object.keys(EstadoCivil);
  sexoValues = Object.keys(Sexo);
  uFValues = Object.keys(UF);
  situacaoAssociadoValues = Object.keys(SituacaoAssociado);

  enderecosCollection: IEndereco[] = [];
  planosSharedCollection: IPlano[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    sobrenome: [],
    dataNascimento: [],
    estadoCivil: [],
    sexo: [],
    naturalidadeUf: [],
    naturalidadeCidade: [],
    numeroDocumento: [],
    ufDocumento: [],
    orgaoDocumento: [],
    dataDocumento: [],
    nomeMae: [],
    nomePai: [],
    email: [],
    telefone: [],
    sitaucao: [],
    endereco: [],
    plano: [],
  });

  constructor(
    protected associadoService: AssociadoService,
    protected enderecoService: EnderecoService,
    protected planoService: PlanoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ associado }) => {
      if (associado.id === undefined) {
        const today = dayjs().startOf('day');
        associado.dataNascimento = today;
        associado.dataDocumento = today;
      }

      this.updateForm(associado);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const associado = this.createFromForm();
    if (associado.id !== undefined) {
      this.subscribeToSaveResponse(this.associadoService.update(associado));
    } else {
      this.subscribeToSaveResponse(this.associadoService.create(associado));
    }
  }

  trackEnderecoById(_index: number, item: IEndereco): number {
    return item.id!;
  }

  trackPlanoById(_index: number, item: IPlano): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssociado>>): void {
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

  protected updateForm(associado: IAssociado): void {
    this.editForm.patchValue({
      id: associado.id,
      nome: associado.nome,
      sobrenome: associado.sobrenome,
      dataNascimento: associado.dataNascimento ? associado.dataNascimento.format(DATE_TIME_FORMAT) : null,
      estadoCivil: associado.estadoCivil,
      sexo: associado.sexo,
      naturalidadeUf: associado.naturalidadeUf,
      naturalidadeCidade: associado.naturalidadeCidade,
      numeroDocumento: associado.numeroDocumento,
      ufDocumento: associado.ufDocumento,
      orgaoDocumento: associado.orgaoDocumento,
      dataDocumento: associado.dataDocumento ? associado.dataDocumento.format(DATE_TIME_FORMAT) : null,
      nomeMae: associado.nomeMae,
      nomePai: associado.nomePai,
      email: associado.email,
      telefone: associado.telefone,
      sitaucao: associado.sitaucao,
      endereco: associado.endereco,
      plano: associado.plano,
    });

    this.enderecosCollection = this.enderecoService.addEnderecoToCollectionIfMissing(this.enderecosCollection, associado.endereco);
    this.planosSharedCollection = this.planoService.addPlanoToCollectionIfMissing(this.planosSharedCollection, associado.plano);
  }

  protected loadRelationshipsOptions(): void {
    this.enderecoService
      .query({ filter: 'associado-is-null' })
      .pipe(map((res: HttpResponse<IEndereco[]>) => res.body ?? []))
      .pipe(
        map((enderecos: IEndereco[]) =>
          this.enderecoService.addEnderecoToCollectionIfMissing(enderecos, this.editForm.get('endereco')!.value)
        )
      )
      .subscribe((enderecos: IEndereco[]) => (this.enderecosCollection = enderecos));

    this.planoService
      .query()
      .pipe(map((res: HttpResponse<IPlano[]>) => res.body ?? []))
      .pipe(map((planos: IPlano[]) => this.planoService.addPlanoToCollectionIfMissing(planos, this.editForm.get('plano')!.value)))
      .subscribe((planos: IPlano[]) => (this.planosSharedCollection = planos));
  }

  protected createFromForm(): IAssociado {
    return {
      ...new Associado(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      sobrenome: this.editForm.get(['sobrenome'])!.value,
      dataNascimento: this.editForm.get(['dataNascimento'])!.value
        ? dayjs(this.editForm.get(['dataNascimento'])!.value, DATE_TIME_FORMAT)
        : undefined,
      estadoCivil: this.editForm.get(['estadoCivil'])!.value,
      sexo: this.editForm.get(['sexo'])!.value,
      naturalidadeUf: this.editForm.get(['naturalidadeUf'])!.value,
      naturalidadeCidade: this.editForm.get(['naturalidadeCidade'])!.value,
      numeroDocumento: this.editForm.get(['numeroDocumento'])!.value,
      ufDocumento: this.editForm.get(['ufDocumento'])!.value,
      orgaoDocumento: this.editForm.get(['orgaoDocumento'])!.value,
      dataDocumento: this.editForm.get(['dataDocumento'])!.value
        ? dayjs(this.editForm.get(['dataDocumento'])!.value, DATE_TIME_FORMAT)
        : undefined,
      nomeMae: this.editForm.get(['nomeMae'])!.value,
      nomePai: this.editForm.get(['nomePai'])!.value,
      email: this.editForm.get(['email'])!.value,
      telefone: this.editForm.get(['telefone'])!.value,
      sitaucao: this.editForm.get(['sitaucao'])!.value,
      endereco: this.editForm.get(['endereco'])!.value,
      plano: this.editForm.get(['plano'])!.value,
    };
  }
}
