import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IConveniado, Conveniado } from '../conveniado.model';
import { ConveniadoService } from '../service/conveniado.service';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { EnderecoService } from 'app/entities/endereco/service/endereco.service';
import { ITipoExame } from 'app/entities/tipo-exame/tipo-exame.model';
import { TipoExameService } from 'app/entities/tipo-exame/service/tipo-exame.service';

@Component({
  selector: 'jhi-conveniado-update',
  templateUrl: './conveniado-update.component.html',
})
export class ConveniadoUpdateComponent implements OnInit {
  isSaving = false;

  enderecosCollection: IEndereco[] = [];
  tipoExamesSharedCollection: ITipoExame[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    telefone: [],
    endereco: [],
    tipoExames: [],
  });

  constructor(
    protected conveniadoService: ConveniadoService,
    protected enderecoService: EnderecoService,
    protected tipoExameService: TipoExameService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conveniado }) => {
      this.updateForm(conveniado);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conveniado = this.createFromForm();
    if (conveniado.id !== undefined) {
      this.subscribeToSaveResponse(this.conveniadoService.update(conveniado));
    } else {
      this.subscribeToSaveResponse(this.conveniadoService.create(conveniado));
    }
  }

  trackEnderecoById(_index: number, item: IEndereco): number {
    return item.id!;
  }

  trackTipoExameById(_index: number, item: ITipoExame): number {
    return item.id!;
  }

  getSelectedTipoExame(option: ITipoExame, selectedVals?: ITipoExame[]): ITipoExame {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConveniado>>): void {
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

  protected updateForm(conveniado: IConveniado): void {
    this.editForm.patchValue({
      id: conveniado.id,
      nome: conveniado.nome,
      telefone: conveniado.telefone,
      endereco: conveniado.endereco,
      tipoExames: conveniado.tipoExames,
    });

    this.enderecosCollection = this.enderecoService.addEnderecoToCollectionIfMissing(this.enderecosCollection, conveniado.endereco);
    this.tipoExamesSharedCollection = this.tipoExameService.addTipoExameToCollectionIfMissing(
      this.tipoExamesSharedCollection,
      ...(conveniado.tipoExames ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.enderecoService
      .query({ filter: 'conveniado-is-null' })
      .pipe(map((res: HttpResponse<IEndereco[]>) => res.body ?? []))
      .pipe(
        map((enderecos: IEndereco[]) =>
          this.enderecoService.addEnderecoToCollectionIfMissing(enderecos, this.editForm.get('endereco')!.value)
        )
      )
      .subscribe((enderecos: IEndereco[]) => (this.enderecosCollection = enderecos));

    this.tipoExameService
      .query()
      .pipe(map((res: HttpResponse<ITipoExame[]>) => res.body ?? []))
      .pipe(
        map((tipoExames: ITipoExame[]) =>
          this.tipoExameService.addTipoExameToCollectionIfMissing(tipoExames, ...(this.editForm.get('tipoExames')!.value ?? []))
        )
      )
      .subscribe((tipoExames: ITipoExame[]) => (this.tipoExamesSharedCollection = tipoExames));
  }

  protected createFromForm(): IConveniado {
    return {
      ...new Conveniado(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      telefone: this.editForm.get(['telefone'])!.value,
      endereco: this.editForm.get(['endereco'])!.value,
      tipoExames: this.editForm.get(['tipoExames'])!.value,
    };
  }
}
