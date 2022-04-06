import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssociado, getAssociadoIdentifier } from '../associado.model';

export type EntityResponseType = HttpResponse<IAssociado>;
export type EntityArrayResponseType = HttpResponse<IAssociado[]>;

@Injectable({ providedIn: 'root' })
export class AssociadoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/associados');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(associado: IAssociado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(associado);
    return this.http
      .post<IAssociado>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(associado: IAssociado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(associado);
    return this.http
      .put<IAssociado>(`${this.resourceUrl}/${getAssociadoIdentifier(associado) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(associado: IAssociado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(associado);
    return this.http
      .patch<IAssociado>(`${this.resourceUrl}/${getAssociadoIdentifier(associado) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssociado>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssociado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssociadoToCollectionIfMissing(
    associadoCollection: IAssociado[],
    ...associadosToCheck: (IAssociado | null | undefined)[]
  ): IAssociado[] {
    const associados: IAssociado[] = associadosToCheck.filter(isPresent);
    if (associados.length > 0) {
      const associadoCollectionIdentifiers = associadoCollection.map(associadoItem => getAssociadoIdentifier(associadoItem)!);
      const associadosToAdd = associados.filter(associadoItem => {
        const associadoIdentifier = getAssociadoIdentifier(associadoItem);
        if (associadoIdentifier == null || associadoCollectionIdentifiers.includes(associadoIdentifier)) {
          return false;
        }
        associadoCollectionIdentifiers.push(associadoIdentifier);
        return true;
      });
      return [...associadosToAdd, ...associadoCollection];
    }
    return associadoCollection;
  }

  protected convertDateFromClient(associado: IAssociado): IAssociado {
    return Object.assign({}, associado, {
      dataNascimento: associado.dataNascimento?.isValid() ? associado.dataNascimento.toJSON() : undefined,
      dataDocumento: associado.dataDocumento?.isValid() ? associado.dataDocumento.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataNascimento = res.body.dataNascimento ? dayjs(res.body.dataNascimento) : undefined;
      res.body.dataDocumento = res.body.dataDocumento ? dayjs(res.body.dataDocumento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((associado: IAssociado) => {
        associado.dataNascimento = associado.dataNascimento ? dayjs(associado.dataNascimento) : undefined;
        associado.dataDocumento = associado.dataDocumento ? dayjs(associado.dataDocumento) : undefined;
      });
    }
    return res;
  }
}
