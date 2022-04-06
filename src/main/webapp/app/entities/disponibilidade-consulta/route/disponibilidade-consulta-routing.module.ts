import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DisponibilidadeConsultaComponent } from '../list/disponibilidade-consulta.component';
import { DisponibilidadeConsultaDetailComponent } from '../detail/disponibilidade-consulta-detail.component';
import { DisponibilidadeConsultaUpdateComponent } from '../update/disponibilidade-consulta-update.component';
import { DisponibilidadeConsultaRoutingResolveService } from './disponibilidade-consulta-routing-resolve.service';

const disponibilidadeConsultaRoute: Routes = [
  {
    path: '',
    component: DisponibilidadeConsultaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DisponibilidadeConsultaDetailComponent,
    resolve: {
      disponibilidadeConsulta: DisponibilidadeConsultaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DisponibilidadeConsultaUpdateComponent,
    resolve: {
      disponibilidadeConsulta: DisponibilidadeConsultaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DisponibilidadeConsultaUpdateComponent,
    resolve: {
      disponibilidadeConsulta: DisponibilidadeConsultaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(disponibilidadeConsultaRoute)],
  exports: [RouterModule],
})
export class DisponibilidadeConsultaRoutingModule {}
