import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICasTraiter } from '../cas-traiter.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-cas-traiter-detail',
  templateUrl: './cas-traiter-detail.component.html',
})
export class CasTraiterDetailComponent implements OnInit {
  casTraiter: ICasTraiter | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ casTraiter }) => {
      this.casTraiter = casTraiter;
      console.warn(this.casTraiter);
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
