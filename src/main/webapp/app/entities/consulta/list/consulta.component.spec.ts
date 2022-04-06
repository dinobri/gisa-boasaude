import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ConsultaService } from '../service/consulta.service';

import { ConsultaComponent } from './consulta.component';

describe('Consulta Management Component', () => {
  let comp: ConsultaComponent;
  let fixture: ComponentFixture<ConsultaComponent>;
  let service: ConsultaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ConsultaComponent],
    })
      .overrideTemplate(ConsultaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConsultaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConsultaService);

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
    expect(comp.consultas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
