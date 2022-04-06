import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrestador } from '../prestador.model';
import { PrestadorService } from '../service/prestador.service';

@Component({
  templateUrl: './prestador-delete-dialog.component.html',
})
export class PrestadorDeleteDialogComponent {
  prestador?: IPrestador;

  constructor(protected prestadorService: PrestadorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prestadorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
