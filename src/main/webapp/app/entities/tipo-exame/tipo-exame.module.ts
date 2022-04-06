import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoExameComponent } from './list/tipo-exame.component';
import { TipoExameDetailComponent } from './detail/tipo-exame-detail.component';
import { TipoExameUpdateComponent } from './update/tipo-exame-update.component';
import { TipoExameDeleteDialogComponent } from './delete/tipo-exame-delete-dialog.component';
import { TipoExameRoutingModule } from './route/tipo-exame-routing.module';

@NgModule({
  imports: [SharedModule, TipoExameRoutingModule],
  declarations: [TipoExameComponent, TipoExameDetailComponent, TipoExameUpdateComponent, TipoExameDeleteDialogComponent],
  entryComponents: [TipoExameDeleteDialogComponent],
})
export class TipoExameModule {}
