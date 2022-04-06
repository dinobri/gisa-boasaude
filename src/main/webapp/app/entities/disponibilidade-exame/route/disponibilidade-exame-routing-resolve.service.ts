import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDisponibilidadeExame, DisponibilidadeExame } from '../disponibilidade-exame.model';
import { DisponibilidadeExameService } from '../service/disponibilidade-exame.service';

@Injectable({ providedIn: 'root' })
export class DisponibilidadeExameRoutingResolveService implements Resolve<IDisponibilidadeExame> {
  constructor(protected service: DisponibilidadeExameService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDisponibilidadeExame> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((disponibilidadeExame: HttpResponse<DisponibilidadeExame>) => {
          if (disponibilidadeExame.body) {
            return of(disponibilidadeExame.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DisponibilidadeExame());
  }
}
