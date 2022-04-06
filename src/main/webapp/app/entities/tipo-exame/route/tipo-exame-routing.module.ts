import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoExameComponent } from '../list/tipo-exame.component';
import { TipoExameDetailComponent } from '../detail/tipo-exame-detail.component';
import { TipoExameUpdateComponent } from '../update/tipo-exame-update.component';
import { TipoExameRoutingResolveService } from './tipo-exame-routing-resolve.service';

const tipoExameRoute: Routes = [
  {
    path: '',
    component: TipoExameComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoExameDetailComponent,
    resolve: {
      tipoExame: TipoExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoExameUpdateComponent,
    resolve: {
      tipoExame: TipoExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoExameUpdateComponent,
    resolve: {
      tipoExame: TipoExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoExameRoute)],
  exports: [RouterModule],
})
export class TipoExameRoutingModule {}
