import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EspecialidadeService } from '../service/especialidade.service';

import { EspecialidadeComponent } from './especialidade.component';

describe('Especialidade Management Component', () => {
  let comp: EspecialidadeComponent;
  let fixture: ComponentFixture<EspecialidadeComponent>;
  let service: EspecialidadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EspecialidadeComponent],
    })
      .overrideTemplate(EspecialidadeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EspecialidadeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EspecialidadeService);

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
    expect(comp.especialidades?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
