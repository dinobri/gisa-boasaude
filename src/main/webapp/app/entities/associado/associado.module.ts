import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssociadoComponent } from './list/associado.component';
import { AssociadoDetailComponent } from './detail/associado-detail.component';
import { AssociadoUpdateComponent } from './update/associado-update.component';
import { AssociadoDeleteDialogComponent } from './delete/associado-delete-dialog.component';
import { AssociadoRoutingModule } from './route/associado-routing.module';

@NgModule({
  imports: [SharedModule, AssociadoRoutingModule],
  declarations: [AssociadoComponent, AssociadoDetailComponent, AssociadoUpdateComponent, AssociadoDeleteDialogComponent],
  entryComponents: [AssociadoDeleteDialogComponent],
})
export class AssociadoModule {}
