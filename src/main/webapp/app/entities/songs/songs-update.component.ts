import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ISongs, Songs } from 'app/shared/model/songs.model';
import { SongsService } from './songs.service';
import { IMusicGenres } from 'app/shared/model/music-genres.model';
import { MusicGenresService } from 'app/entities/music-genres';
import { IAlbums } from 'app/shared/model/albums.model';
import { AlbumsService } from 'app/entities/albums';
import { IListDetails } from 'app/shared/model/list-details.model';
import { ListDetailsService } from 'app/entities/list-details';

@Component({
  selector: 'jhi-songs-update',
  templateUrl: './songs-update.component.html'
})
export class SongsUpdateComponent implements OnInit {
  songs: ISongs;
  isSaving: boolean;

  musicgenres: IMusicGenres[];

  albums: IAlbums[];

  listdetails: IListDetails[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    picture: [],
    pictureContentType: [],
    duration: [],
    audio: [],
    audioContentType: [],
    artists: [],
    musicGenreId: [],
    albumId: [],
    listDetailsId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected songsService: SongsService,
    protected musicGenresService: MusicGenresService,
    protected albumsService: AlbumsService,
    protected listDetailsService: ListDetailsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ songs }) => {
      this.updateForm(songs);
      this.songs = songs;
    });
    this.musicGenresService
      .query({ filter: 'songs-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IMusicGenres[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMusicGenres[]>) => response.body)
      )
      .subscribe(
        (res: IMusicGenres[]) => {
          if (!this.songs.musicGenreId) {
            this.musicgenres = res;
          } else {
            this.musicGenresService
              .find(this.songs.musicGenreId)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IMusicGenres>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IMusicGenres>) => subResponse.body)
              )
              .subscribe(
                (subRes: IMusicGenres) => (this.musicgenres = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.albumsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IAlbums[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAlbums[]>) => response.body)
      )
      .subscribe((res: IAlbums[]) => (this.albums = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.listDetailsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IListDetails[]>) => mayBeOk.ok),
        map((response: HttpResponse<IListDetails[]>) => response.body)
      )
      .subscribe((res: IListDetails[]) => (this.listdetails = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(songs: ISongs) {
    this.editForm.patchValue({
      id: songs.id,
      name: songs.name,
      picture: songs.picture,
      pictureContentType: songs.pictureContentType,
      duration: songs.duration,
      audio: songs.audio,
      audioContentType: songs.audioContentType,
      artists: songs.artists,
      musicGenreId: songs.musicGenreId,
      albumId: songs.albumId,
      listDetailsId: songs.listDetailsId
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const songs = this.createFromForm();
    if (songs.id !== undefined) {
      this.subscribeToSaveResponse(this.songsService.update(songs));
    } else {
      this.subscribeToSaveResponse(this.songsService.create(songs));
    }
  }

  private createFromForm(): ISongs {
    const entity = {
      ...new Songs(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      pictureContentType: this.editForm.get(['pictureContentType']).value,
      picture: this.editForm.get(['picture']).value,
      duration: this.editForm.get(['duration']).value,
      audioContentType: this.editForm.get(['audioContentType']).value,
      audio: this.editForm.get(['audio']).value,
      artists: this.editForm.get(['artists']).value,
      musicGenreId: this.editForm.get(['musicGenreId']).value,
      albumId: this.editForm.get(['albumId']).value,
      listDetailsId: this.editForm.get(['listDetailsId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISongs>>) {
    result.subscribe((res: HttpResponse<ISongs>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackMusicGenresById(index: number, item: IMusicGenres) {
    return item.id;
  }

  trackAlbumsById(index: number, item: IAlbums) {
    return item.id;
  }

  trackListDetailsById(index: number, item: IListDetails) {
    return item.id;
  }
}
