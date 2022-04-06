import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DisponibilidadeConsultaService } from '../service/disponibilidade-consulta.service';

import { DisponibilidadeConsultaComponent } from './disponibilidade-consulta.component';

describe('DisponibilidadeConsulta Management Component', () => {
  let comp: DisponibilidadeConsultaComponent;
  let fixture: ComponentFixture<DisponibilidadeConsultaComponent>;
  let service: DisponibilidadeConsultaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DisponibilidadeConsultaComponent],
    })
      .overrideTemplate(DisponibilidadeConsultaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisponibilidadeConsultaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DisponibilidadeConsultaService);

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
    expect(comp.disponibilidadeConsultas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
