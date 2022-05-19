import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlace } from '../place.model';
import { PlaceService } from '../service/place.service';

@Component({
  templateUrl: './place-delete-dialog.component.html',
})
export class PlaceDeleteDialogComponent {
  place?: IPlace;

  constructor(protected placeService: PlaceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.placeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
