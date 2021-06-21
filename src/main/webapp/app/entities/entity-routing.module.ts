import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'corporate',
        data: { pageTitle: 'balsamTestApp.corporate.home.title' },
        loadChildren: () => import('./corporate/corporate.module').then(m => m.CorporateModule),
      },
      {
        path: 'membership',
        data: { pageTitle: 'balsamTestApp.membership.home.title' },
        loadChildren: () => import('./membership/membership.module').then(m => m.MembershipModule),
      },
      {
        path: 'validation-request',
        data: { pageTitle: 'balsamTestApp.validationRequest.home.title' },
        loadChildren: () => import('./validation-request/validation-request.module').then(m => m.ValidationRequestModule),
      },
      {
        path: 'validation-request-file',
        data: { pageTitle: 'balsamTestApp.validationRequestFile.home.title' },
        loadChildren: () => import('./validation-request-file/validation-request-file.module').then(m => m.ValidationRequestFileModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'balsamTestApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'city',
        data: { pageTitle: 'balsamTestApp.city.home.title' },
        loadChildren: () => import('./city/city.module').then(m => m.CityModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'balsamTestApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'service-package-type',
        data: { pageTitle: 'balsamTestApp.servicePackageType.home.title' },
        loadChildren: () => import('./service-package-type/service-package-type.module').then(m => m.ServicePackageTypeModule),
      },
      {
        path: 'service-package',
        data: { pageTitle: 'balsamTestApp.servicePackage.home.title' },
        loadChildren: () => import('./service-package/service-package.module').then(m => m.ServicePackageModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
