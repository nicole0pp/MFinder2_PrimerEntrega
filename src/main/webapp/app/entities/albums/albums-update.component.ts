import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IAlbums, Albums } from 'app/shared/model/albums.model';
import { AlbumsService } from './albums.service';

@Component({
  selector: 'jhi-albums-update',
  templateUrl: './albums-update.component.html'
})
export class AlbumsUpdateComponent implements OnInit {
  albums: IAlbums;
  isSaving: boolean;
  publicationDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    picture: [],
    pictureContentType: [],
    publicationDate: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected albumsService: AlbumsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ albums }) => {
      this.updateForm(albums);
      this.albums = albums;
    });
  }

  updateForm(albums: IAlbums) {
    this.editForm.patchValue({
      id: albums.id,
      name: albums.name,
      picture: albums.picture,
      pictureContentType: albums.pictureContentType,
      publicationDate: albums.publicationDate
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
    const albums = this.createFromForm();
    if (albums.id !== undefined) {
      this.subscribeToSaveResponse(this.albumsService.update(albums));
    } else {
      this.subscribeToSaveResponse(this.albumsService.create(albums));
    }
  }

  private createFromForm(): IAlbums {
    const entity = {
      ...new Albums(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      pictureContentType: this.editForm.get(['pictureContentType']).value,
      picture: this.editForm.get(['picture']).value,
      publicationDate: this.editForm.get(['publicationDate']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlbums>>) {
    result.subscribe((res: HttpResponse<IAlbums>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
