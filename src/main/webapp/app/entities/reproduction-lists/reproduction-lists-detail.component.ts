import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IReproductionLists } from 'app/shared/model/reproduction-lists.model';

@Component({
  selector: 'jhi-reproduction-lists-detail',
  templateUrl: './reproduction-lists-detail.component.html'
})
export class ReproductionListsDetailComponent implements OnInit {
  reproductionLists: IReproductionLists;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ reproductionLists }) => {
      this.reproductionLists = reproductionLists;
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
