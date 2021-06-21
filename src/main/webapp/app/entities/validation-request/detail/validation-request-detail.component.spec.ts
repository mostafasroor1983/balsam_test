import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ValidationRequestDetailComponent } from './validation-request-detail.component';

describe('Component Tests', () => {
  describe('ValidationRequest Management Detail Component', () => {
    let comp: ValidationRequestDetailComponent;
    let fixture: ComponentFixture<ValidationRequestDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ValidationRequestDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ validationRequest: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ValidationRequestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ValidationRequestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load validationRequest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.validationRequest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
