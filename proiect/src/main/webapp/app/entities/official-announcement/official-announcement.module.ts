import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertComponent } from './list/alert.component';
import { OfficialAnnouncementUpdateComponent } from './update/official-announcement-update.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SharedModule } from '../../shared/shared.module';
import { OfficialAnnouncementRoutingModule } from './route/official-announcement-routing.module';

@NgModule({
  declarations: [AlertComponent, OfficialAnnouncementUpdateComponent],
  imports: [CommonModule, FontAwesomeModule, SharedModule, OfficialAnnouncementRoutingModule],
})
export class OfficialAnnouncementModule {}
