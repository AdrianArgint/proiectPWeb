import { TestBed } from '@angular/core/testing';

import { OfficialAnnouncementResolver } from './official-announcement.resolver';

describe('OfficialAnnouncementResolver', () => {
  let resolver: OfficialAnnouncementResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(OfficialAnnouncementResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
