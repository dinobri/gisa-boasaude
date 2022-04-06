import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssociado, Associado } from '../associado.model';
import { AssociadoService } from '../service/associado.service';

@Injectable({ providedIn: 'root' })
export class AssociadoRoutingResolveService implements Resolve<IAssociado> {
  constructor(protected service: AssociadoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssociado> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((associado: HttpResponse<Associado>) => {
          if (associado.body) {
            return of(associado.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Associado());
  }
}
