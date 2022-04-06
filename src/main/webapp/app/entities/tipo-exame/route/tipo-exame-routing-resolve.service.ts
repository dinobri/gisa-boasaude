import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoExame, TipoExame } from '../tipo-exame.model';
import { TipoExameService } from '../service/tipo-exame.service';

@Injectable({ providedIn: 'root' })
export class TipoExameRoutingResolveService implements Resolve<ITipoExame> {
  constructor(protected service: TipoExameService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoExame> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoExame: HttpResponse<TipoExame>) => {
          if (tipoExame.body) {
            return of(tipoExame.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoExame());
  }
}
