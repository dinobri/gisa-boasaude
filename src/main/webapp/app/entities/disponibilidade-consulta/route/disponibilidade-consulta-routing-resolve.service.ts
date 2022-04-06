import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDisponibilidadeConsulta, DisponibilidadeConsulta } from '../disponibilidade-consulta.model';
import { DisponibilidadeConsultaService } from '../service/disponibilidade-consulta.service';

@Injectable({ providedIn: 'root' })
export class DisponibilidadeConsultaRoutingResolveService implements Resolve<IDisponibilidadeConsulta> {
  constructor(protected service: DisponibilidadeConsultaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDisponibilidadeConsulta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((disponibilidadeConsulta: HttpResponse<DisponibilidadeConsulta>) => {
          if (disponibilidadeConsulta.body) {
            return of(disponibilidadeConsulta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DisponibilidadeConsulta());
  }
}
