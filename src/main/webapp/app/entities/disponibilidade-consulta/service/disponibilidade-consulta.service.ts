import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDisponibilidadeConsulta, getDisponibilidadeConsultaIdentifier } from '../disponibilidade-consulta.model';

export type EntityResponseType = HttpResponse<IDisponibilidadeConsulta>;
export type EntityArrayResponseType = HttpResponse<IDisponibilidadeConsulta[]>;

@Injectable({ providedIn: 'root' })
export class DisponibilidadeConsultaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/disponibilidade-consultas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(disponibilidadeConsulta: IDisponibilidadeConsulta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(disponibilidadeConsulta);
    return this.http
      .post<IDisponibilidadeConsulta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(disponibilidadeConsulta: IDisponibilidadeConsulta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(disponibilidadeConsulta);
    return this.http
      .put<IDisponibilidadeConsulta>(
        `${this.resourceUrl}/${getDisponibilidadeConsultaIdentifier(disponibilidadeConsulta) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(disponibilidadeConsulta: IDisponibilidadeConsulta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(disponibilidadeConsulta);
    return this.http
      .patch<IDisponibilidadeConsulta>(
        `${this.resourceUrl}/${getDisponibilidadeConsultaIdentifier(disponibilidadeConsulta) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDisponibilidadeConsulta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDisponibilidadeConsulta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDisponibilidadeConsultaToCollectionIfMissing(
    disponibilidadeConsultaCollection: IDisponibilidadeConsulta[],
    ...disponibilidadeConsultasToCheck: (IDisponibilidadeConsulta | null | undefined)[]
  ): IDisponibilidadeConsulta[] {
    const disponibilidadeConsultas: IDisponibilidadeConsulta[] = disponibilidadeConsultasToCheck.filter(isPresent);
    if (disponibilidadeConsultas.length > 0) {
      const disponibilidadeConsultaCollectionIdentifiers = disponibilidadeConsultaCollection.map(
        disponibilidadeConsultaItem => getDisponibilidadeConsultaIdentifier(disponibilidadeConsultaItem)!
      );
      const disponibilidadeConsultasToAdd = disponibilidadeConsultas.filter(disponibilidadeConsultaItem => {
        const disponibilidadeConsultaIdentifier = getDisponibilidadeConsultaIdentifier(disponibilidadeConsultaItem);
        if (
          disponibilidadeConsultaIdentifier == null ||
          disponibilidadeConsultaCollectionIdentifiers.includes(disponibilidadeConsultaIdentifier)
        ) {
          return false;
        }
        disponibilidadeConsultaCollectionIdentifiers.push(disponibilidadeConsultaIdentifier);
        return true;
      });
      return [...disponibilidadeConsultasToAdd, ...disponibilidadeConsultaCollection];
    }
    return disponibilidadeConsultaCollection;
  }

  protected convertDateFromClient(disponibilidadeConsulta: IDisponibilidadeConsulta): IDisponibilidadeConsulta {
    return Object.assign({}, disponibilidadeConsulta, {
      horaInicio: disponibilidadeConsulta.horaInicio?.isValid() ? disponibilidadeConsulta.horaInicio.toJSON() : undefined,
      horaFim: disponibilidadeConsulta.horaFim?.isValid() ? disponibilidadeConsulta.horaFim.toJSON() : undefined,
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
      res.body.forEach((disponibilidadeConsulta: IDisponibilidadeConsulta) => {
        disponibilidadeConsulta.horaInicio = disponibilidadeConsulta.horaInicio ? dayjs(disponibilidadeConsulta.horaInicio) : undefined;
        disponibilidadeConsulta.horaFim = disponibilidadeConsulta.horaFim ? dayjs(disponibilidadeConsulta.horaFim) : undefined;
      });
    }
    return res;
  }
}
