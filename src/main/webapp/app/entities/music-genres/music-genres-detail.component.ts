import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMusicGenres } from 'app/shared/model/music-genres.model';

@Component({
  selector: 'jhi-music-genres-detail',
  templateUrl: './music-genres-detail.component.html'
})
export class MusicGenresDetailComponent implements OnInit {
  musicGenres: IMusicGenres;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ musicGenres }) => {
      this.musicGenres = musicGenres;
    });
  }

  previousState() {
    window.history.back();
  }
}
