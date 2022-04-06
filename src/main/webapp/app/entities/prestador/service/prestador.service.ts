import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrestador, getPrestadorIdentifier } from '../prestador.model';

export type EntityResponseType = HttpResponse<IPrestador>;
export type EntityArrayResponseType = HttpResponse<IPrestador[]>;

@Injectable({ providedIn: 'root' })
export class PrestadorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prestadors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prestador: IPrestador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prestador);
    return this.http
      .post<IPrestador>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(prestador: IPrestador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prestador);
    return this.http
      .put<IPrestador>(`${this.resourceUrl}/${getPrestadorIdentifier(prestador) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(prestador: IPrestador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prestador);
    return this.http
      .patch<IPrestador>(`${this.resourceUrl}/${getPrestadorIdentifier(prestador) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrestador>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrestador[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPrestadorToCollectionIfMissing(
    prestadorCollection: IPrestador[],
    ...prestadorsToCheck: (IPrestador | null | undefined)[]
  ): IPrestador[] {
    const prestadors: IPrestador[] = prestadorsToCheck.filter(isPresent);
    if (prestadors.length > 0) {
      const prestadorCollectionIdentifiers = prestadorCollection.map(prestadorItem => getPrestadorIdentifier(prestadorItem)!);
      const prestadorsToAdd = prestadors.filter(prestadorItem => {
        const prestadorIdentifier = getPrestadorIdentifier(prestadorItem);
        if (prestadorIdentifier == null || prestadorCollectionIdentifiers.includes(prestadorIdentifier)) {
          return false;
        }
        prestadorCollectionIdentifiers.push(prestadorIdentifier);
        return true;
      });
      return [...prestadorsToAdd, ...prestadorCollection];
    }
    return prestadorCollection;
  }

  protected convertDateFromClient(prestador: IPrestador): IPrestador {
    return Object.assign({}, prestador, {
      dataNascimento: prestador.dataNascimento?.isValid() ? prestador.dataNascimento.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataNascimento = res.body.dataNascimento ? dayjs(res.body.dataNascimento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((prestador: IPrestador) => {
        prestador.dataNascimento = prestador.dataNascimento ? dayjs(prestador.dataNascimento) : undefined;
      });
    }
    return res;
  }
}
