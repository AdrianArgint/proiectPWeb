import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlace, Place } from '../place.model';

import { PlaceService } from './place.service';

describe('Place Service', () => {
  let service: PlaceService;
  let httpMock: HttpTestingController;
  let elemDefault: IPlace;
  let expectedResult: IPlace | IPlace[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlaceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      latitude: 0,
      longitude: 0,
      address: 'AAAAAAA',
      type: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Place', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Place()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Place', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          latitude: 1,
          longitude: 1,
          address: 'BBBBBB',
          type: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Place', () => {
      const patchObject = Object.assign(
        {
          latitude: 1,
          type: 'BBBBBB',
          description: 'BBBBBB',
        },
        new Place()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Place', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          latitude: 1,
          longitude: 1,
          address: 'BBBBBB',
          type: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Place', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPlaceToCollectionIfMissing', () => {
      it('should add a Place to an empty array', () => {
        const place: IPlace = { id: 123 };
        expectedResult = service.addPlaceToCollectionIfMissing([], place);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(place);
      });

      it('should not add a Place to an array that contains it', () => {
        const place: IPlace = { id: 123 };
        const placeCollection: IPlace[] = [
          {
            ...place,
          },
          { id: 456 },
        ];
        expectedResult = service.addPlaceToCollectionIfMissing(placeCollection, place);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Place to an array that doesn't contain it", () => {
        const place: IPlace = { id: 123 };
        const placeCollection: IPlace[] = [{ id: 456 }];
        expectedResult = service.addPlaceToCollectionIfMissing(placeCollection, place);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(place);
      });

      it('should add only unique Place to an array', () => {
        const placeArray: IPlace[] = [{ id: 123 }, { id: 456 }, { id: 92874 }];
        const placeCollection: IPlace[] = [{ id: 123 }];
        expectedResult = service.addPlaceToCollectionIfMissing(placeCollection, ...placeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const place: IPlace = { id: 123 };
        const place2: IPlace = { id: 456 };
        expectedResult = service.addPlaceToCollectionIfMissing([], place, place2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(place);
        expect(expectedResult).toContain(place2);
      });

      it('should accept null and undefined values', () => {
        const place: IPlace = { id: 123 };
        expectedResult = service.addPlaceToCollectionIfMissing([], null, place, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(place);
      });

      it('should return initial array if no Place is added', () => {
        const placeCollection: IPlace[] = [{ id: 123 }];
        expectedResult = service.addPlaceToCollectionIfMissing(placeCollection, undefined, null);
        expect(expectedResult).toEqual(placeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
