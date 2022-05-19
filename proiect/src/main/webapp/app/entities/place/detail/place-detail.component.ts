import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlace } from '../place.model';
import { IUser } from '../../user/user.model';
import { UserService } from '../../user/user.service';

@Component({
  selector: 'jhi-place-detail',
  templateUrl: './place-detail.component.html',
})
export class PlaceDetailComponent implements OnInit {
  place: IPlace | null = null;
  checkInPeople?: IUser[];

  constructor(protected activatedRoute: ActivatedRoute, protected userService: UserService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ place }) => {
      this.place = place;
      this.userService.queryPlace(place.id).subscribe(res => {
        if (res.body) {
          this.checkInPeople = res.body;
        }
      });
    });
  }

  previousState(): void {
    window.history.back();
  }

  getCheckedInPeopleList(): string {
    if (!this.checkInPeople || this.checkInPeople.length === 0) {
      return ' No one checked-in yet';
    }
    return this.checkInPeople
      .map(person => {
        if (person.firstName && person.lastName) {
          return `${person.firstName} ${person.lastName}`;
        } else if (person.firstName && !person.lastName) {
          return `${person.firstName}`;
        } else if (!person.firstName && person.lastName) {
          return `${person.lastName}`;
        } else {
          return '';
        }
      })
      .join(' ');
  }
}
