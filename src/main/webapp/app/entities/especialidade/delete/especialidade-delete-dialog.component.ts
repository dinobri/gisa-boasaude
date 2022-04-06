import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEspecialidade } from '../especialidade.model';
import { EspecialidadeService } from '../service/especialidade.service';

@Component({
  templateUrl: './especialidade-delete-dialog.component.html',
})
export class EspecialidadeDeleteDialogComponent {
  especialidade?: IEspecialidade;

  constructor(protected especialidadeService: EspecialidadeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.especialidadeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
