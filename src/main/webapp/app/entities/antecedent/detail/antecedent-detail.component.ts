import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAntecedent } from '../antecedent.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-antecedent-detail',
  templateUrl: './antecedent-detail.component.html',
})
export class AntecedentDetailComponent implements OnInit {
  antecedent: IAntecedent | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ antecedent }) => {
      this.antecedent = antecedent;
      console.warn(this.antecedent);
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
