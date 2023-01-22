import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IFavoriteList, FavoriteList } from 'app/shared/model/reproduction-lists.model';
import { FavoriteListService } from './reproduction-lists.service';

@Component({
  selector: 'jhi-reproduction-lists-update',
  templateUrl: './reproduction-lists-update.component.html'
})
export class FavoriteListUpdateComponent implements OnInit {
  FavoriteList: IFavoriteList;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    picture: [],
    pictureContentType: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected FavoriteListService: FavoriteListService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ FavoriteList }) => {
      this.updateForm(FavoriteList);
      this.FavoriteList = FavoriteList;
    });
  }

  updateForm(FavoriteList: IFavoriteList) {
    this.editForm.patchValue({
      id: FavoriteList.id,
      name: FavoriteList.name,
      picture: FavoriteList.picture,
      pictureContentType: FavoriteList.pictureContentType
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
    const FavoriteList = this.createFromForm();
    if (FavoriteList.id !== undefined) {
      this.subscribeToSaveResponse(this.FavoriteListService.update(FavoriteList));
    } else {
      this.subscribeToSaveResponse(this.FavoriteListService.create(FavoriteList));
    }
  }

  private createFromForm(): IFavoriteList {
    const entity = {
      ...new FavoriteList(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      pictureContentType: this.editForm.get(['pictureContentType']).value,
      picture: this.editForm.get(['picture']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFavoriteList>>) {
    result.subscribe((res: HttpResponse<IFavoriteList>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
