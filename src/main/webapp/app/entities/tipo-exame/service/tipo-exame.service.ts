import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoExame, getTipoExameIdentifier } from '../tipo-exame.model';

export type EntityResponseType = HttpResponse<ITipoExame>;
export type EntityArrayResponseType = HttpResponse<ITipoExame[]>;

@Injectable({ providedIn: 'root' })
export class TipoExameService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-exames');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoExame: ITipoExame): Observable<EntityResponseType> {
    return this.http.post<ITipoExame>(this.resourceUrl, tipoExame, { observe: 'response' });
  }

  update(tipoExame: ITipoExame): Observable<EntityResponseType> {
    return this.http.put<ITipoExame>(`${this.resourceUrl}/${getTipoExameIdentifier(tipoExame) as number}`, tipoExame, {
      observe: 'response',
    });
  }

  partialUpdate(tipoExame: ITipoExame): Observable<EntityResponseType> {
    return this.http.patch<ITipoExame>(`${this.resourceUrl}/${getTipoExameIdentifier(tipoExame) as number}`, tipoExame, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoExame>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoExame[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoExameToCollectionIfMissing(
    tipoExameCollection: ITipoExame[],
    ...tipoExamesToCheck: (ITipoExame | null | undefined)[]
  ): ITipoExame[] {
    const tipoExames: ITipoExame[] = tipoExamesToCheck.filter(isPresent);
    if (tipoExames.length > 0) {
      const tipoExameCollectionIdentifiers = tipoExameCollection.map(tipoExameItem => getTipoExameIdentifier(tipoExameItem)!);
      const tipoExamesToAdd = tipoExames.filter(tipoExameItem => {
        const tipoExameIdentifier = getTipoExameIdentifier(tipoExameItem);
        if (tipoExameIdentifier == null || tipoExameCollectionIdentifiers.includes(tipoExameIdentifier)) {
          return false;
        }
        tipoExameCollectionIdentifiers.push(tipoExameIdentifier);
        return true;
      });
      return [...tipoExamesToAdd, ...tipoExameCollection];
    }
    return tipoExameCollection;
  }
}
