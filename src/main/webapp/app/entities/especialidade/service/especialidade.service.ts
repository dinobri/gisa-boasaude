import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEspecialidade, getEspecialidadeIdentifier } from '../especialidade.model';

export type EntityResponseType = HttpResponse<IEspecialidade>;
export type EntityArrayResponseType = HttpResponse<IEspecialidade[]>;

@Injectable({ providedIn: 'root' })
export class EspecialidadeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/especialidades');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(especialidade: IEspecialidade): Observable<EntityResponseType> {
    return this.http.post<IEspecialidade>(this.resourceUrl, especialidade, { observe: 'response' });
  }

  update(especialidade: IEspecialidade): Observable<EntityResponseType> {
    return this.http.put<IEspecialidade>(`${this.resourceUrl}/${getEspecialidadeIdentifier(especialidade) as number}`, especialidade, {
      observe: 'response',
    });
  }

  partialUpdate(especialidade: IEspecialidade): Observable<EntityResponseType> {
    return this.http.patch<IEspecialidade>(`${this.resourceUrl}/${getEspecialidadeIdentifier(especialidade) as number}`, especialidade, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEspecialidade>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEspecialidade[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEspecialidadeToCollectionIfMissing(
    especialidadeCollection: IEspecialidade[],
    ...especialidadesToCheck: (IEspecialidade | null | undefined)[]
  ): IEspecialidade[] {
    const especialidades: IEspecialidade[] = especialidadesToCheck.filter(isPresent);
    if (especialidades.length > 0) {
      const especialidadeCollectionIdentifiers = especialidadeCollection.map(
        especialidadeItem => getEspecialidadeIdentifier(especialidadeItem)!
      );
      const especialidadesToAdd = especialidades.filter(especialidadeItem => {
        const especialidadeIdentifier = getEspecialidadeIdentifier(especialidadeItem);
        if (especialidadeIdentifier == null || especialidadeCollectionIdentifiers.includes(especialidadeIdentifier)) {
          return false;
        }
        especialidadeCollectionIdentifiers.push(especialidadeIdentifier);
        return true;
      });
      return [...especialidadesToAdd, ...especialidadeCollection];
    }
    return especialidadeCollection;
  }
}
