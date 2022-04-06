import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDisponibilidadeExame, getDisponibilidadeExameIdentifier } from '../disponibilidade-exame.model';

export type EntityResponseType = HttpResponse<IDisponibilidadeExame>;
export type EntityArrayResponseType = HttpResponse<IDisponibilidadeExame[]>;

@Injectable({ providedIn: 'root' })
export class DisponibilidadeExameService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/disponibilidade-exames');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(disponibilidadeExame: IDisponibilidadeExame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(disponibilidadeExame);
    return this.http
      .post<IDisponibilidadeExame>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(disponibilidadeExame: IDisponibilidadeExame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(disponibilidadeExame);
    return this.http
      .put<IDisponibilidadeExame>(`${this.resourceUrl}/${getDisponibilidadeExameIdentifier(disponibilidadeExame) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(disponibilidadeExame: IDisponibilidadeExame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(disponibilidadeExame);
    return this.http
      .patch<IDisponibilidadeExame>(`${this.resourceUrl}/${getDisponibilidadeExameIdentifier(disponibilidadeExame) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDisponibilidadeExame>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDisponibilidadeExame[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDisponibilidadeExameToCollectionIfMissing(
    disponibilidadeExameCollection: IDisponibilidadeExame[],
    ...disponibilidadeExamesToCheck: (IDisponibilidadeExame | null | undefined)[]
  ): IDisponibilidadeExame[] {
    const disponibilidadeExames: IDisponibilidadeExame[] = disponibilidadeExamesToCheck.filter(isPresent);
    if (disponibilidadeExames.length > 0) {
      const disponibilidadeExameCollectionIdentifiers = disponibilidadeExameCollection.map(
        disponibilidadeExameItem => getDisponibilidadeExameIdentifier(disponibilidadeExameItem)!
      );
      const disponibilidadeExamesToAdd = disponibilidadeExames.filter(disponibilidadeExameItem => {
        const disponibilidadeExameIdentifier = getDisponibilidadeExameIdentifier(disponibilidadeExameItem);
        if (disponibilidadeExameIdentifier == null || disponibilidadeExameCollectionIdentifiers.includes(disponibilidadeExameIdentifier)) {
          return false;
        }
        disponibilidadeExameCollectionIdentifiers.push(disponibilidadeExameIdentifier);
        return true;
      });
      return [...disponibilidadeExamesToAdd, ...disponibilidadeExameCollection];
    }
    return disponibilidadeExameCollection;
  }

  protected convertDateFromClient(disponibilidadeExame: IDisponibilidadeExame): IDisponibilidadeExame {
    return Object.assign({}, disponibilidadeExame, {
      horaInicio: disponibilidadeExame.horaInicio?.isValid() ? disponibilidadeExame.horaInicio.toJSON() : undefined,
      horaFim: disponibilidadeExame.horaFim?.isValid() ? disponibilidadeExame.horaFim.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.horaInicio = res.body.horaInicio ? dayjs(res.body.horaInicio) : undefined;
      res.body.horaFim = res.body.horaFim ? dayjs(res.body.horaFim) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((disponibilidadeExame: IDisponibilidadeExame) => {
        disponibilidadeExame.horaInicio = disponibilidadeExame.horaInicio ? dayjs(disponibilidadeExame.horaInicio) : undefined;
        disponibilidadeExame.horaFim = disponibilidadeExame.horaFim ? dayjs(disponibilidadeExame.horaFim) : undefined;
      });
    }
    return res;
  }
}
