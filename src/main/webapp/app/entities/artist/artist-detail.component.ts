import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArtist } from 'app/shared/model/artist.model';

@Component({
  selector: 'jhi-artist-detail',
  templateUrl: './artist-detail.component.html'
})
export class ArtistDetailComponent implements OnInit {
  artist: IArtist;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ artist }) => {
      this.artist = artist;
    });
  }

  previousState() {
    window.history.back();
  }
}
