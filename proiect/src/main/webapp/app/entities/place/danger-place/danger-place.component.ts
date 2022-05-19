import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlace } from '../place.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { PlaceService } from '../service/place.service';
import { PlaceDeleteDialogComponent } from '../delete/place-delete-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { AccountService } from '../../../core/auth/account.service';
import { UserService } from '../../user/user.service';
import { IUser } from '../../user/user.model';
import { OverviewDialogComponent } from '../help-place/overview-dialog.component';

@Component({
  selector: 'jhi-danger-place',
  templateUrl: './danger-place.component.html',
})
export class DangerPlaceComponent implements OnInit {
  places?: IPlace[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  user?: IUser;

  constructor(
    protected placeService: PlaceService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    public dialog: MatDialog,
    public accountService: AccountService,
    public userService: UserService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.placeService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        'type.equals': 'Danger',
      })
      .subscribe({
        next: (res: HttpResponse<IPlace[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.userService.findByLogin(account!.login).subscribe(res => {
        if (res.body) {
          this.user = res.body;
        }
      });
    });
    this.handleNavigation();
  }

  trackId(index: number, item: IPlace): number {
    return item.id!;
  }

  delete(place: IPlace): void {
    const modalRef = this.modalService.open(PlaceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.place = place;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  openConfirmationDialog(place: IPlace): void {
    const dialogRef = this.dialog.open(OverviewDialogComponent, {
      width: '450px',
      data: { bool: this.user!.place?.id !== undefined },
    });

    dialogRef.afterClosed().subscribe(() => {
      this.user!.place = place;
      this.userService.update(this.user!).subscribe();
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IPlace[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/place/danger'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
          'type.equals': 'Danger',
        },
      });
    }
    this.places = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
