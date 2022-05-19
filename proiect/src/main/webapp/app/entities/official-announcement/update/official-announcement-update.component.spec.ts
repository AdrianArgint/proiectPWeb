import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficialAnnouncementUpdateComponent } from './official-announcement-update.component';

describe('OfficialAnnouncementUpdateComponent', () => {
  let component: OfficialAnnouncementUpdateComponent;
  let fixture: ComponentFixture<OfficialAnnouncementUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OfficialAnnouncementUpdateComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfficialAnnouncementUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
