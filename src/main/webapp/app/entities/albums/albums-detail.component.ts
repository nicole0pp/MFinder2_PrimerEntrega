import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IAlbums } from 'app/shared/model/albums.model';

@Component({
  selector: 'jhi-albums-detail',
  templateUrl: './albums-detail.component.html'
})
export class AlbumsDetailComponent implements OnInit {
  albums: IAlbums;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ albums }) => {
      this.albums = albums;
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
