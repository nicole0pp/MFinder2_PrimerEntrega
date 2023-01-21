import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IArtist, Artist } from 'app/shared/model/artist.model';
import { ArtistService } from './artist.service';

@Component({
  selector: 'jhi-artist-update',
  templateUrl: './artist-update.component.html'
})
export class ArtistUpdateComponent implements OnInit {
  artist: IArtist;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    artistic_name: [null, [Validators.required]]
  });

  constructor(protected artistService: ArtistService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ artist }) => {
      this.updateForm(artist);
      this.artist = artist;
    });
  }

  updateForm(artist: IArtist) {
    this.editForm.patchValue({
      id: artist.id,
      artistic_name: artist.artistic_name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const artist = this.createFromForm();
    if (artist.id !== undefined) {
      this.subscribeToSaveResponse(this.artistService.update(artist));
    } else {
      this.subscribeToSaveResponse(this.artistService.create(artist));
    }
  }

  private createFromForm(): IArtist {
    const entity = {
      ...new Artist(),
      id: this.editForm.get(['id']).value,
      artistic_name: this.editForm.get(['artistic_name']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArtist>>) {
    result.subscribe((res: HttpResponse<IArtist>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
