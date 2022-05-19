import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Account } from '../core/auth/account.model';
import { AccountService } from '../core/auth/account.service';
import { UserService } from '../entities/user/user.service';
import { IUser } from '../entities/user/user.model';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  providers: [MessageService],
})
export class ProfileComponent implements OnInit {
  account: Account | null = null;
  user?: IUser;
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    email: [],
    login: [],
    place: [],
  });

  constructor(
    private accountService: AccountService,
    protected fb: FormBuilder,
    private userService: UserService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.account = account;
      this.userService.findByLogin(this.account!.login).subscribe(res => {
        if (res.body) {
          this.user = res.body;
          this.updateForm();
        }
      });
    });
  }

  save(): void {
    this.isSaving = true;
    const user = this.createFromForm();
    this.subscribeToSaveResponse(this.userService.update(user));
  }

  previousState(): void {
    window.history.back();
  }

  protected updateForm(): void {
    this.editForm.patchValue({
      id: this.user?.id,
      firstName: this.user?.firstName,
      login: this.user?.login,
      lastName: this.user?.lastName,
      email: this.user?.email,
      place: this.user?.place,
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res),
      () => this.onSaveError()
    );
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected onSaveSuccess(res: HttpResponse<IUser>): void {
    if (res.body) {
      this.user = res.body;
      this.messageService.add({
        severity: 'success',
        summary: 'Salvat cu succes',
        detail: 'Informatiile contului dvs au fost actualizate !',
      });
    }
  }

  protected onSaveError(): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Din nefericire a intervenit o eroare!',
      detail: 'Va rugam sa reincercati.',
    });
  }

  protected createFromForm(): IUser {
    return {
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      login: this.editForm.get(['login'])!.value,
      place: this.editForm.get(['place'])!.value,
    };
  }
}
