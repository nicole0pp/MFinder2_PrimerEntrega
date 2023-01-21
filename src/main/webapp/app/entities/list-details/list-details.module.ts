import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MFinder2SharedModule } from 'app/shared';
import {
  ListDetailsComponent,
  ListDetailsDetailComponent,
  ListDetailsUpdateComponent,
  ListDetailsDeletePopupComponent,
  ListDetailsDeleteDialogComponent,
  listDetailsRoute,
  listDetailsPopupRoute
} from './';

const ENTITY_STATES = [...listDetailsRoute, ...listDetailsPopupRoute];

@NgModule({
  imports: [MFinder2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ListDetailsComponent,
    ListDetailsDetailComponent,
    ListDetailsUpdateComponent,
    ListDetailsDeleteDialogComponent,
    ListDetailsDeletePopupComponent
  ],
  entryComponents: [ListDetailsComponent, ListDetailsUpdateComponent, ListDetailsDeleteDialogComponent, ListDetailsDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2ListDetailsModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
