import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrestador, Prestador } from '../prestador.model';
import { PrestadorService } from '../service/prestador.service';

@Injectable({ providedIn: 'root' })
export class PrestadorRoutingResolveService implements Resolve<IPrestador> {
  constructor(protected service: PrestadorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrestador> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prestador: HttpResponse<Prestador>) => {
          if (prestador.body) {
            return of(prestador.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Prestador());
  }
}
