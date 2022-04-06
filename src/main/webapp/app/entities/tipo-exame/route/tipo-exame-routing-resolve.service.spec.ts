import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITipoExame, TipoExame } from '../tipo-exame.model';
import { TipoExameService } from '../service/tipo-exame.service';

import { TipoExameRoutingResolveService } from './tipo-exame-routing-resolve.service';

describe('TipoExame routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoExameRoutingResolveService;
  let service: TipoExameService;
  let resultTipoExame: ITipoExame | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TipoExameRoutingResolveService);
    service = TestBed.inject(TipoExameService);
    resultTipoExame = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoExame returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoExame = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoExame).toEqual({ id: 123 });
    });

    it('should return new ITipoExame if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoExame = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoExame).toEqual(new TipoExame());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoExame })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoExame = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoExame).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
