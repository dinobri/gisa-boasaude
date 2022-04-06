import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrestadorComponent } from './list/prestador.component';
import { PrestadorDetailComponent } from './detail/prestador-detail.component';
import { PrestadorUpdateComponent } from './update/prestador-update.component';
import { PrestadorDeleteDialogComponent } from './delete/prestador-delete-dialog.component';
import { PrestadorRoutingModule } from './route/prestador-routing.module';

@NgModule({
  imports: [SharedModule, PrestadorRoutingModule],
  declarations: [PrestadorComponent, PrestadorDetailComponent, PrestadorUpdateComponent, PrestadorDeleteDialogComponent],
  entryComponents: [PrestadorDeleteDialogComponent],
})
export class PrestadorModule {}
