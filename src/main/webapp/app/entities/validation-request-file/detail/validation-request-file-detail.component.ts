import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValidationRequestFile } from '../validation-request-file.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-validation-request-file-detail',
  templateUrl: './validation-request-file-detail.component.html',
})
export class ValidationRequestFileDetailComponent implements OnInit {
  validationRequestFile: IValidationRequestFile | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ validationRequestFile }) => {
      this.validationRequestFile = validationRequestFile;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
