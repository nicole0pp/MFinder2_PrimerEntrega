import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMusicGenre } from 'app/shared/model/music-genres.model';

@Component({
  selector: 'jhi-music-genres-detail',
  templateUrl: './music-genres-detail.component.html'
})
export class MusicGenreDetailComponent implements OnInit {
  MusicGenre: IMusicGenre;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ MusicGenre }) => {
      this.MusicGenre = MusicGenre;
    });
  }

  previousState() {
    window.history.back();
  }
}
