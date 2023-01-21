/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { MFinder2TestModule } from '../../../test.module';
import { ListDetailsUpdateComponent } from 'app/entities/list-details/list-details-update.component';
import { ListDetailsService } from 'app/entities/list-details/list-details.service';
import { ListDetails } from 'app/shared/model/list-details.model';

describe('Component Tests', () => {
  describe('ListDetails Management Update Component', () => {
    let comp: ListDetailsUpdateComponent;
    let fixture: ComponentFixture<ListDetailsUpdateComponent>;
    let service: ListDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MFinder2TestModule],
        declarations: [ListDetailsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ListDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ListDetailsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ListDetailsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ListDetails(123);
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
        const entity = new ListDetails();
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
