import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IFavoriteList } from 'app/shared/model/reproduction-lists.model';

@Component({
  selector: 'jhi-reproduction-lists-detail',
  templateUrl: './reproduction-lists-detail.component.html'
})
export class FavoriteListDetailComponent implements OnInit {
  FavoriteList: IFavoriteList;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ FavoriteList }) => {
      this.FavoriteList = FavoriteList;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
