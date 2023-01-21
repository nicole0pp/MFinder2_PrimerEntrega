import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IMusicGenres, MusicGenres } from 'app/shared/model/music-genres.model';
import { MusicGenresService } from './music-genres.service';

@Component({
  selector: 'jhi-music-genres-update',
  templateUrl: './music-genres-update.component.html'
})
export class MusicGenresUpdateComponent implements OnInit {
  musicGenres: IMusicGenres;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    type: []
  });

  constructor(protected musicGenresService: MusicGenresService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ musicGenres }) => {
      this.updateForm(musicGenres);
      this.musicGenres = musicGenres;
    });
  }

  updateForm(musicGenres: IMusicGenres) {
    this.editForm.patchValue({
      id: musicGenres.id,
      name: musicGenres.name,
      type: musicGenres.type
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const musicGenres = this.createFromForm();
    if (musicGenres.id !== undefined) {
      this.subscribeToSaveResponse(this.musicGenresService.update(musicGenres));
    } else {
      this.subscribeToSaveResponse(this.musicGenresService.create(musicGenres));
    }
  }

  private createFromForm(): IMusicGenres {
    const entity = {
      ...new MusicGenres(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      type: this.editForm.get(['type']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMusicGenres>>) {
    result.subscribe((res: HttpResponse<IMusicGenres>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
