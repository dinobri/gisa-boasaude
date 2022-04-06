import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DisponibilidadeExameComponent } from './list/disponibilidade-exame.component';
import { DisponibilidadeExameDetailComponent } from './detail/disponibilidade-exame-detail.component';
import { DisponibilidadeExameUpdateComponent } from './update/disponibilidade-exame-update.component';
import { DisponibilidadeExameDeleteDialogComponent } from './delete/disponibilidade-exame-delete-dialog.component';
import { DisponibilidadeExameRoutingModule } from './route/disponibilidade-exame-routing.module';

@NgModule({
  imports: [SharedModule, DisponibilidadeExameRoutingModule],
  declarations: [
    DisponibilidadeExameComponent,
    DisponibilidadeExameDetailComponent,
    DisponibilidadeExameUpdateComponent,
    DisponibilidadeExameDeleteDialogComponent,
  ],
  entryComponents: [DisponibilidadeExameDeleteDialogComponent],
})
export class DisponibilidadeExameModule {}
