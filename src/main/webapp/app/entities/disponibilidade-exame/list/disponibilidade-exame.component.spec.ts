import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DisponibilidadeExameService } from '../service/disponibilidade-exame.service';

import { DisponibilidadeExameComponent } from './disponibilidade-exame.component';

describe('DisponibilidadeExame Management Component', () => {
  let comp: DisponibilidadeExameComponent;
  let fixture: ComponentFixture<DisponibilidadeExameComponent>;
  let service: DisponibilidadeExameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DisponibilidadeExameComponent],
    })
      .overrideTemplate(DisponibilidadeExameComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisponibilidadeExameComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DisponibilidadeExameService);

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
    expect(comp.disponibilidadeExames?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
