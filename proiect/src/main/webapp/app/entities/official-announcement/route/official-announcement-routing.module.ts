import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OfficialAnnouncementUpdateComponent } from '../update/official-announcement-update.component';
import { OfficialAnnouncementResolver } from './official-announcement.resolver';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';
import { AlertComponent } from '../list/alert.component';

const announcementRoute: Routes = [
  {
    path: 'new',
    component: OfficialAnnouncementUpdateComponent,
    resolve: {
      announcement: OfficialAnnouncementResolver,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: '',
    component: AlertComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(announcementRoute)],
  exports: [RouterModule],
})
export class OfficialAnnouncementRoutingModule {}
