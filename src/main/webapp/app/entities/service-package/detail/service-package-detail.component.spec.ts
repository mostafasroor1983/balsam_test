import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServicePackageDetailComponent } from './service-package-detail.component';

describe('Component Tests', () => {
  describe('ServicePackage Management Detail Component', () => {
    let comp: ServicePackageDetailComponent;
    let fixture: ComponentFixture<ServicePackageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ServicePackageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ servicePackage: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ServicePackageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServicePackageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load servicePackage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.servicePackage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
