import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EspecialidadeComponent } from './list/especialidade.component';
import { EspecialidadeDetailComponent } from './detail/especialidade-detail.component';
import { EspecialidadeUpdateComponent } from './update/especialidade-update.component';
import { EspecialidadeDeleteDialogComponent } from './delete/especialidade-delete-dialog.component';
import { EspecialidadeRoutingModule } from './route/especialidade-routing.module';

@NgModule({
  imports: [SharedModule, EspecialidadeRoutingModule],
  declarations: [EspecialidadeComponent, EspecialidadeDetailComponent, EspecialidadeUpdateComponent, EspecialidadeDeleteDialogComponent],
  entryComponents: [EspecialidadeDeleteDialogComponent],
})
export class EspecialidadeModule {}
