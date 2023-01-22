/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { SongUpdateComponent } from 'app/entities/Song/Song-update.component';
import { SongService } from 'app/entities/Song/Song.service';
import { Song } from 'app/shared/model/Song.model';

describe('Component Tests', () => {
  describe('Song Management Update Component', () => {
    let comp: SongUpdateComponent;
    let fixture: ComponentFixture<SongUpdateComponent>;
    let service: SongService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [SongUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SongUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SongUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SongService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Song(123);
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
        const entity = new Song();
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
