/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { FavoriteListUpdateComponent } from 'app/entities/reproduction-lists/reproduction-lists-update.component';
import { FavoriteListService } from 'app/entities/reproduction-lists/reproduction-lists.service';
import { FavoriteList } from 'app/shared/model/reproduction-lists.model';

describe('Component Tests', () => {
  describe('FavoriteList Management Update Component', () => {
    let comp: FavoriteListUpdateComponent;
    let fixture: ComponentFixture<FavoriteListUpdateComponent>;
    let service: FavoriteListService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [FavoriteListUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FavoriteListUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FavoriteListUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FavoriteListService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FavoriteList(123);
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
        const entity = new FavoriteList();
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
