import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrEchographie } from '../cr-echographie.model';

@Component({
  selector: 'jhi-cr-echographie-detail',
  templateUrl: './cr-echographie-detail.component.html',
})
export class CrEchographieDetailComponent implements OnInit {
  crEchographie: ICrEchographie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crEchographie }) => {
      this.crEchographie = crEchographie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
