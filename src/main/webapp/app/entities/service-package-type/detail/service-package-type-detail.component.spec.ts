import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServicePackageTypeDetailComponent } from './service-package-type-detail.component';

describe('Component Tests', () => {
  describe('ServicePackageType Management Detail Component', () => {
    let comp: ServicePackageTypeDetailComponent;
    let fixture: ComponentFixture<ServicePackageTypeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ServicePackageTypeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ servicePackageType: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ServicePackageTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServicePackageTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load servicePackageType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.servicePackageType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
