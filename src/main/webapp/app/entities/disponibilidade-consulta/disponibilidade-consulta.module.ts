import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DisponibilidadeConsultaComponent } from './list/disponibilidade-consulta.component';
import { DisponibilidadeConsultaDetailComponent } from './detail/disponibilidade-consulta-detail.component';
import { DisponibilidadeConsultaUpdateComponent } from './update/disponibilidade-consulta-update.component';
import { DisponibilidadeConsultaDeleteDialogComponent } from './delete/disponibilidade-consulta-delete-dialog.component';
import { DisponibilidadeConsultaRoutingModule } from './route/disponibilidade-consulta-routing.module';

@NgModule({
  imports: [SharedModule, DisponibilidadeConsultaRoutingModule],
  declarations: [
    DisponibilidadeConsultaComponent,
    DisponibilidadeConsultaDetailComponent,
    DisponibilidadeConsultaUpdateComponent,
    DisponibilidadeConsultaDeleteDialogComponent,
  ],
  entryComponents: [DisponibilidadeConsultaDeleteDialogComponent],
})
export class DisponibilidadeConsultaModule {}
