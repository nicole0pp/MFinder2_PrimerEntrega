import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IMusicGenre, MusicGenre } from 'app/shared/model/music-genres.model';
import { MusicGenreService } from './music-genres.service';

@Component({
  selector: 'jhi-music-genres-update',
  templateUrl: './music-genres-update.component.html'
})
export class MusicGenreUpdateComponent implements OnInit {
  MusicGenre: IMusicGenre;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    type: []
  });

  constructor(protected MusicGenreService: MusicGenreService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ MusicGenre }) => {
      this.updateForm(MusicGenre);
      this.MusicGenre = MusicGenre;
    });
  }

  updateForm(MusicGenre: IMusicGenre) {
    this.editForm.patchValue({
      id: MusicGenre.id,
      name: MusicGenre.name,
      type: MusicGenre.type
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const MusicGenre = this.createFromForm();
    if (MusicGenre.id !== undefined) {
      this.subscribeToSaveResponse(this.MusicGenreService.update(MusicGenre));
    } else {
      this.subscribeToSaveResponse(this.MusicGenreService.create(MusicGenre));
    }
  }

  private createFromForm(): IMusicGenre {
    const entity = {
      ...new MusicGenre(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      type: this.editForm.get(['type']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMusicGenre>>) {
    result.subscribe((res: HttpResponse<IMusicGenre>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
