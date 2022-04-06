import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssociadoComponent } from '../list/associado.component';
import { AssociadoDetailComponent } from '../detail/associado-detail.component';
import { AssociadoUpdateComponent } from '../update/associado-update.component';
import { AssociadoRoutingResolveService } from './associado-routing-resolve.service';

const associadoRoute: Routes = [
  {
    path: '',
    component: AssociadoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssociadoDetailComponent,
    resolve: {
      associado: AssociadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssociadoUpdateComponent,
    resolve: {
      associado: AssociadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssociadoUpdateComponent,
    resolve: {
      associado: AssociadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(associadoRoute)],
  exports: [RouterModule],
})
export class AssociadoRoutingModule {}
