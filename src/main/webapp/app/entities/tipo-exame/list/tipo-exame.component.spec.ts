import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TipoExameService } from '../service/tipo-exame.service';

import { TipoExameComponent } from './tipo-exame.component';

describe('TipoExame Management Component', () => {
  let comp: TipoExameComponent;
  let fixture: ComponentFixture<TipoExameComponent>;
  let service: TipoExameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoExameComponent],
    })
      .overrideTemplate(TipoExameComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoExameComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TipoExameService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.tipoExames?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
