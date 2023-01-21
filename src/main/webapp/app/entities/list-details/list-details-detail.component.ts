import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IListDetails } from 'app/shared/model/list-details.model';

@Component({
  selector: 'jhi-list-details-detail',
  templateUrl: './list-details-detail.component.html'
})
export class ListDetailsDetailComponent implements OnInit {
  listDetails: IListDetails;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ listDetails }) => {
      this.listDetails = listDetails;
    });
  }

  previousState() {
    window.history.back();
  }
}
