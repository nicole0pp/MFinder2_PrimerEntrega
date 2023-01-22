import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IAlbum } from 'app/shared/model/Album.model';

@Component({
  selector: 'jhi-Album-detail',
  templateUrl: './Album-detail.component.html'
})
export class AlbumDetailComponent implements OnInit {
  Album: IAlbum;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ Album }) => {
      this.Album = Album;
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
