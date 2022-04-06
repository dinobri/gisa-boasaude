import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoExame } from '../tipo-exame.model';
import { TipoExameService } from '../service/tipo-exame.service';

@Component({
  templateUrl: './tipo-exame-delete-dialog.component.html',
})
export class TipoExameDeleteDialogComponent {
  tipoExame?: ITipoExame;

  constructor(protected tipoExameService: TipoExameService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoExameService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
