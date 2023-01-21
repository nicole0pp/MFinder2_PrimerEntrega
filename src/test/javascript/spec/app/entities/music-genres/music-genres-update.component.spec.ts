/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { MusicGenresUpdateComponent } from 'app/entities/music-genres/music-genres-update.component';
import { MusicGenresService } from 'app/entities/music-genres/music-genres.service';
import { MusicGenres } from 'app/shared/model/music-genres.model';

describe('Component Tests', () => {
  describe('MusicGenres Management Update Component', () => {
    let comp: MusicGenresUpdateComponent;
    let fixture: ComponentFixture<MusicGenresUpdateComponent>;
    let service: MusicGenresService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [MusicGenresUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MusicGenresUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MusicGenresUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MusicGenresService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MusicGenres(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MusicGenres();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
