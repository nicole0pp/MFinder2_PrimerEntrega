import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IAlbum, Album } from 'app/shared/model/Album.model';
import { AlbumService } from './Album.service';

@Component({
  selector: 'jhi-Album-update',
  templateUrl: './Album-update.component.html'
})
export class AlbumUpdateComponent implements OnInit {
  Album: IAlbum;
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
    protected AlbumService: AlbumService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ Album }) => {
      this.updateForm(Album);
      this.Album = Album;
    });
  }

  updateForm(Album: IAlbum) {
    this.editForm.patchValue({
      id: Album.id,
      name: Album.name,
      picture: Album.picture,
      pictureContentType: Album.pictureContentType,
      publicationDate: Album.publicationDate
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
    const Album = this.createFromForm();
    if (Album.id !== undefined) {
      this.subscribeToSaveResponse(this.AlbumService.update(Album));
    } else {
      this.subscribeToSaveResponse(this.AlbumService.create(Album));
    }
  }

  private createFromForm(): IAlbum {
    const entity = {
      ...new Album(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      pictureContentType: this.editForm.get(['pictureContentType']).value,
      picture: this.editForm.get(['picture']).value,
      publicationDate: this.editForm.get(['publicationDate']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlbum>>) {
    result.subscribe((res: HttpResponse<IAlbum>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
