import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEspecialidade, Especialidade } from '../especialidade.model';
import { EspecialidadeService } from '../service/especialidade.service';

import { EspecialidadeRoutingResolveService } from './especialidade-routing-resolve.service';

describe('Especialidade routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EspecialidadeRoutingResolveService;
  let service: EspecialidadeService;
  let resultEspecialidade: IEspecialidade | undefined;

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
    routingResolveService = TestBed.inject(EspecialidadeRoutingResolveService);
    service = TestBed.inject(EspecialidadeService);
    resultEspecialidade = undefined;
  });

  describe('resolve', () => {
    it('should return IEspecialidade returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEspecialidade = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEspecialidade).toEqual({ id: 123 });
    });

    it('should return new IEspecialidade if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEspecialidade = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEspecialidade).toEqual(new Especialidade());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Especialidade })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEspecialidade = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEspecialidade).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
