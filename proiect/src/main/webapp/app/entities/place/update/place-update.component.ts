import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPlace, Place } from '../place.model';
import { PlaceService } from '../service/place.service';
import { placesTypes } from '../../../app.constants';

@Component({
  selector: 'jhi-place-update',
  templateUrl: './place-update.component.html',
  styleUrls: ['./place-update.component.scss'],
})
export class PlaceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: ['', [Validators.required]],
    latitude: [],
    longitude: [],
    address: ['', [Validators.required]],
    type: [],
    description: ['', [Validators.required]],
    addressForm: ['', [Validators.required]],
  });
  placeTypes = placesTypes;
  placeTypeForm = '';
  constructor(protected placeService: PlaceService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ place }) => {
      this.updateForm(place);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const place = this.createFromForm();
    if (place.id !== undefined) {
      this.subscribeToSaveResponse(this.placeService.update(place));
    } else {
      this.subscribeToSaveResponse(this.placeService.create(place));
    }
  }

  onAddressChange($event: any): void {
    console.log($event);
    this.editForm.patchValue({
      address: $event.name,
      latitude: $event.geometry.location.lat(),
      longitude: $event.geometry.location.lng(),
      addressForm: $event,
    });
    console.log($event.name);
    console.log(this.editForm.get(['address'])!.value);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlace>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(place: IPlace): void {
    this.editForm.patchValue({
      id: place.id,
      name: place.name,
      latitude: place.latitude,
      longitude: place.longitude,
      address: place.address,
      type: place.type,
      description: place.description,
      addressForm: place.address,
    });
    console.log(this.editForm.get(['address'])!.value);
    console.log(this.editForm.get(['addressForm'])!.value);

    this.placeTypeForm = this.editForm.get(['type'])!.value;
  }

  protected createFromForm(): IPlace {
    return {
      ...new Place(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      address: this.editForm.get(['address'])!.value,
      type: this.placeTypeForm,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
