import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EspecialidadeComponent } from '../list/especialidade.component';
import { EspecialidadeDetailComponent } from '../detail/especialidade-detail.component';
import { EspecialidadeUpdateComponent } from '../update/especialidade-update.component';
import { EspecialidadeRoutingResolveService } from './especialidade-routing-resolve.service';

const especialidadeRoute: Routes = [
  {
    path: '',
    component: EspecialidadeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EspecialidadeDetailComponent,
    resolve: {
      especialidade: EspecialidadeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EspecialidadeUpdateComponent,
    resolve: {
      especialidade: EspecialidadeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EspecialidadeUpdateComponent,
    resolve: {
      especialidade: EspecialidadeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(especialidadeRoute)],
  exports: [RouterModule],
})
export class EspecialidadeRoutingModule {}
