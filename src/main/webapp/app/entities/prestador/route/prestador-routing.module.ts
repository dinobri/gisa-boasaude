import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrestadorComponent } from '../list/prestador.component';
import { PrestadorDetailComponent } from '../detail/prestador-detail.component';
import { PrestadorUpdateComponent } from '../update/prestador-update.component';
import { PrestadorRoutingResolveService } from './prestador-routing-resolve.service';

const prestadorRoute: Routes = [
  {
    path: '',
    component: PrestadorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrestadorDetailComponent,
    resolve: {
      prestador: PrestadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrestadorUpdateComponent,
    resolve: {
      prestador: PrestadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrestadorUpdateComponent,
    resolve: {
      prestador: PrestadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prestadorRoute)],
  exports: [RouterModule],
})
export class PrestadorRoutingModule {}
