import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { AlertService } from '../service/alert.service';
import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from '../../../config/input.constants';
import { IAlert } from '../alert.model';
import { finalize } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-official-announcement-update',
  templateUrl: './official-announcement-update.component.html',
  styleUrls: ['./official-announcement-update.component.scss'],
})
export class OfficialAnnouncementUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
    content: [],
    createdDate: [],
  });

  constructor(protected activatedRoute: ActivatedRoute, protected fb: FormBuilder, protected announcementService: AlertService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ announcement }) => {
      if (announcement.id === undefined) {
        const today = dayjs().startOf('day');
        announcement.createdDate = today;
        announcement.lastUpdatedDate = today;
      }

      this.updateForm(announcement);
    });
  }

  updateForm(announcement: IAlert): void {
    this.editForm.patchValue({
      id: announcement.id,
      title: announcement.title,
      content: announcement.content,
      createdDate: announcement.createdDate ? announcement.createdDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  createFromForm(): IAlert {
    return {
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }

  previousState(): void {
    window.history.back();
  }

  onSaveSuccess(): void {
    this.previousState();
  }

  onSaveError(): void {
    // Api for inheritance.
  }

  onSaveFinalize(): void {
    this.isSaving = false;
  }

  subscribeToSaveResponse(result: Observable<HttpResponse<IAlert>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  save(): void {
    this.isSaving = true;
    const post = this.createFromForm();
    this.subscribeToSaveResponse(this.announcementService.create(post));
  }
}
