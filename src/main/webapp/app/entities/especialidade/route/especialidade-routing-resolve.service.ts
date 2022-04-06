import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEspecialidade, Especialidade } from '../especialidade.model';
import { EspecialidadeService } from '../service/especialidade.service';

@Injectable({ providedIn: 'root' })
export class EspecialidadeRoutingResolveService implements Resolve<IEspecialidade> {
  constructor(protected service: EspecialidadeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEspecialidade> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((especialidade: HttpResponse<Especialidade>) => {
          if (especialidade.body) {
            return of(especialidade.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Especialidade());
  }
}
