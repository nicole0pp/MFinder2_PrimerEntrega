/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { AlbumsUpdateComponent } from 'app/entities/albums/albums-update.component';
import { AlbumsService } from 'app/entities/albums/albums.service';
import { Albums } from 'app/shared/model/albums.model';

describe('Component Tests', () => {
  describe('Albums Management Update Component', () => {
    let comp: AlbumsUpdateComponent;
    let fixture: ComponentFixture<AlbumsUpdateComponent>;
    let service: AlbumsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [AlbumsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AlbumsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AlbumsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AlbumsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Albums(123);
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
        const entity = new Albums();
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
