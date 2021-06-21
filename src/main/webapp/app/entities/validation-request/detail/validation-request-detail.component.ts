import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValidationRequest } from '../validation-request.model';

@Component({
  selector: 'jhi-validation-request-detail',
  templateUrl: './validation-request-detail.component.html',
})
export class ValidationRequestDetailComponent implements OnInit {
  validationRequest: IValidationRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ validationRequest }) => {
      this.validationRequest = validationRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
