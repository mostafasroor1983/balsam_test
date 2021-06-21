import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICorporate } from '../corporate.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-corporate-detail',
  templateUrl: './corporate-detail.component.html',
})
export class CorporateDetailComponent implements OnInit {
  corporate: ICorporate | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corporate }) => {
      this.corporate = corporate;
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
