import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssociado } from '../associado.model';
import { AssociadoService } from '../service/associado.service';

@Component({
  templateUrl: './associado-delete-dialog.component.html',
})
export class AssociadoDeleteDialogComponent {
  associado?: IAssociado;

  constructor(protected associadoService: AssociadoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.associadoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
