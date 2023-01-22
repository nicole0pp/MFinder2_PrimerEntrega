import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ISong, Song } from 'app/shared/model/Song.model';
import { SongService } from './Song.service';
import { IMusicGenre } from 'app/shared/model/music-genres.model';
import { MusicGenreService } from 'app/entities/music-genres';
import { IAlbum } from 'app/shared/model/Album.model';
import { AlbumService } from 'app/entities/Album';
import { IListDetails } from 'app/shared/model/list-details.model';
import { ListDetailsService } from 'app/entities/list-details';

@Component({
  selector: 'jhi-Song-update',
  templateUrl: './Song-update.component.html'
})
export class SongUpdateComponent implements OnInit {
  Song: ISong;
  isSaving: boolean;

  MusicGenre: IMusicGenre[];

  Album: IAlbum[];

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
    AlbumId: [],
    listDetailsId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected SongService: SongService,
    protected MusicGenreService: MusicGenreService,
    protected AlbumService: AlbumService,
    protected listDetailsService: ListDetailsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ Song }) => {
      this.updateForm(Song);
      this.Song = Song;
    });
    this.MusicGenreService.query({ filter: 'Song-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IMusicGenre[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMusicGenre[]>) => response.body)
      )
      .subscribe(
        (res: IMusicGenre[]) => {
          if (!this.Song.musicGenreId) {
            this.MusicGenre = res;
          } else {
            this.MusicGenreService.find(this.Song.musicGenreId)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IMusicGenre>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IMusicGenre>) => subResponse.body)
              )
              .subscribe(
                (subRes: IMusicGenre) => (this.MusicGenre = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.AlbumService.query()
      .pipe(
        filter((mayBeOk: HttpResponse<IAlbum[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAlbum[]>) => response.body)
      )
      .subscribe((res: IAlbum[]) => (this.Album = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.listDetailsService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IListDetails[]>) => mayBeOk.ok),
        map((response: HttpResponse<IListDetails[]>) => response.body)
      )
      .subscribe((res: IListDetails[]) => (this.listdetails = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(Song: ISong) {
    this.editForm.patchValue({
      id: Song.id,
      name: Song.name,
      picture: Song.picture,
      pictureContentType: Song.pictureContentType,
      duration: Song.duration,
      audio: Song.audio,
      audioContentType: Song.audioContentType,
      artists: Song.artists,
      musicGenreId: Song.musicGenreId,
      AlbumId: Song.AlbumId,
      listDetailsId: Song.listDetailsId
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
    const Song = this.createFromForm();
    if (Song.id !== undefined) {
      this.subscribeToSaveResponse(this.SongService.update(Song));
    } else {
      this.subscribeToSaveResponse(this.SongService.create(Song));
    }
  }

  private createFromForm(): ISong {
    const entity = {
      ...new Song(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      pictureContentType: this.editForm.get(['pictureContentType']).value,
      picture: this.editForm.get(['picture']).value,
      duration: this.editForm.get(['duration']).value,
      audioContentType: this.editForm.get(['audioContentType']).value,
      audio: this.editForm.get(['audio']).value,
      artists: this.editForm.get(['artists']).value,
      musicGenreId: this.editForm.get(['musicGenreId']).value,
      AlbumId: this.editForm.get(['AlbumId']).value,
      listDetailsId: this.editForm.get(['listDetailsId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISong>>) {
    result.subscribe((res: HttpResponse<ISong>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackMusicGenreById(index: number, item: IMusicGenre) {
    return item.id;
  }

  trackAlbumById(index: number, item: IAlbum) {
    return item.id;
  }

  trackListDetailsById(index: number, item: IListDetails) {
    return item.id;
  }
}
