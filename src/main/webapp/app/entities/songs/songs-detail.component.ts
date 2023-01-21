import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISongs } from 'app/shared/model/songs.model';

@Component({
  selector: 'jhi-songs-detail',
  templateUrl: './songs-detail.component.html'
})
export class SongsDetailComponent implements OnInit {
  songs: ISongs;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ songs }) => {
      this.songs = songs;
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
