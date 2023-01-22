import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MFinder2SharedModule } from 'app/shared';
import {
  FavoriteListComponent,
  FavoriteListDetailComponent,
  FavoriteListUpdateComponent,
  FavoriteListDeletePopupComponent,
  FavoriteListDeleteDialogComponent,
  FavoriteListRoute,
  FavoriteListPopupRoute
} from './';

const ENTITY_STATES = [...FavoriteListRoute, ...FavoriteListPopupRoute];

@NgModule({
  imports: [MFinder2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FavoriteListComponent,
    FavoriteListDetailComponent,
    FavoriteListUpdateComponent,
    FavoriteListDeleteDialogComponent,
    FavoriteListDeletePopupComponent
  ],
  entryComponents: [
    FavoriteListComponent,
    FavoriteListUpdateComponent,
    FavoriteListDeleteDialogComponent,
    FavoriteListDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2FavoriteListModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
