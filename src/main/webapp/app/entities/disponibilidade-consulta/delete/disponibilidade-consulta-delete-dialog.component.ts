import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDisponibilidadeConsulta } from '../disponibilidade-consulta.model';
import { DisponibilidadeConsultaService } from '../service/disponibilidade-consulta.service';

@Component({
  templateUrl: './disponibilidade-consulta-delete-dialog.component.html',
})
export class DisponibilidadeConsultaDeleteDialogComponent {
  disponibilidadeConsulta?: IDisponibilidadeConsulta;

  constructor(protected disponibilidadeConsultaService: DisponibilidadeConsultaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.disponibilidadeConsultaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
