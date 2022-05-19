import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlaceDetailComponent } from './place-detail.component';

describe('Place Management Detail Component', () => {
  let comp: PlaceDetailComponent;
  let fixture: ComponentFixture<PlaceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlaceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ place: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlaceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlaceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load place on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.place).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
