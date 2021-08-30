import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICertificat } from '../certificat.model';

@Component({
  selector: 'jhi-certificat-detail',
  templateUrl: './certificat-detail.component.html',
})
export class CertificatDetailComponent implements OnInit {
  certificat: ICertificat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ certificat }) => {
      this.certificat = certificat;
      console.warn(this.certificat);
    });
  }

  previousState(): void {
    window.history.back();
  }
}
