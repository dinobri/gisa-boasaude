import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDisponibilidadeExame } from '../disponibilidade-exame.model';
import { DisponibilidadeExameService } from '../service/disponibilidade-exame.service';

@Component({
  templateUrl: './disponibilidade-exame-delete-dialog.component.html',
})
export class DisponibilidadeExameDeleteDialogComponent {
  disponibilidadeExame?: IDisponibilidadeExame;

  constructor(protected disponibilidadeExameService: DisponibilidadeExameService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.disponibilidadeExameService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
