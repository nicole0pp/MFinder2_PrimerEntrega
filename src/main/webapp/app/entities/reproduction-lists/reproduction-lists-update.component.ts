import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IReproductionLists, ReproductionLists } from 'app/shared/model/reproduction-lists.model';
import { ReproductionListsService } from './reproduction-lists.service';

@Component({
  selector: 'jhi-reproduction-lists-update',
  templateUrl: './reproduction-lists-update.component.html'
})
export class ReproductionListsUpdateComponent implements OnInit {
  reproductionLists: IReproductionLists;
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
    protected reproductionListsService: ReproductionListsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ reproductionLists }) => {
      this.updateForm(reproductionLists);
      this.reproductionLists = reproductionLists;
    });
  }

  updateForm(reproductionLists: IReproductionLists) {
    this.editForm.patchValue({
      id: reproductionLists.id,
      name: reproductionLists.name,
      picture: reproductionLists.picture,
      pictureContentType: reproductionLists.pictureContentType
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
    const reproductionLists = this.createFromForm();
    if (reproductionLists.id !== undefined) {
      this.subscribeToSaveResponse(this.reproductionListsService.update(reproductionLists));
    } else {
      this.subscribeToSaveResponse(this.reproductionListsService.create(reproductionLists));
    }
  }

  private createFromForm(): IReproductionLists {
    const entity = {
      ...new ReproductionLists(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      pictureContentType: this.editForm.get(['pictureContentType']).value,
      picture: this.editForm.get(['picture']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReproductionLists>>) {
    result.subscribe((res: HttpResponse<IReproductionLists>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
