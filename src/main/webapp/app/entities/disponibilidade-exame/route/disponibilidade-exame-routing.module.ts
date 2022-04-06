import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DisponibilidadeExameComponent } from '../list/disponibilidade-exame.component';
import { DisponibilidadeExameDetailComponent } from '../detail/disponibilidade-exame-detail.component';
import { DisponibilidadeExameUpdateComponent } from '../update/disponibilidade-exame-update.component';
import { DisponibilidadeExameRoutingResolveService } from './disponibilidade-exame-routing-resolve.service';

const disponibilidadeExameRoute: Routes = [
  {
    path: '',
    component: DisponibilidadeExameComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DisponibilidadeExameDetailComponent,
    resolve: {
      disponibilidadeExame: DisponibilidadeExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DisponibilidadeExameUpdateComponent,
    resolve: {
      disponibilidadeExame: DisponibilidadeExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DisponibilidadeExameUpdateComponent,
    resolve: {
      disponibilidadeExame: DisponibilidadeExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(disponibilidadeExameRoute)],
  exports: [RouterModule],
})
export class DisponibilidadeExameRoutingModule {}
