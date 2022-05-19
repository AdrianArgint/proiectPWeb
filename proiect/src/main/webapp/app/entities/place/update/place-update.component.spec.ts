import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlaceService } from '../service/place.service';
import { IPlace, Place } from '../place.model';

import { PlaceUpdateComponent } from './place-update.component';

describe('Place Management Update Component', () => {
  let comp: PlaceUpdateComponent;
  let fixture: ComponentFixture<PlaceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let placeService: PlaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlaceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PlaceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    placeService = TestBed.inject(PlaceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const place: IPlace = { id: 456 };

      activatedRoute.data = of({ place });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(place));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Place>>();
      const place = { id: 123 };
      jest.spyOn(placeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ place });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: place }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(placeService.update).toHaveBeenCalledWith(place);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Place>>();
      const place = new Place();
      jest.spyOn(placeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ place });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: place }));
      saveSubject.complete();

      // THEN
      expect(placeService.create).toHaveBeenCalledWith(place);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Place>>();
      const place = { id: 123 };
      jest.spyOn(placeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ place });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(placeService.update).toHaveBeenCalledWith(place);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
