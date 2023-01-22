import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MFinder2SharedModule } from 'app/shared';
import {
  MusicGenreComponent,
  MusicGenreDetailComponent,
  MusicGenreUpdateComponent,
  MusicGenreDeletePopupComponent,
  MusicGenreDeleteDialogComponent,
  MusicGenreRoute,
  MusicGenrePopupRoute
} from './';

const ENTITY_STATES = [...MusicGenreRoute, ...MusicGenrePopupRoute];

@NgModule({
  imports: [MFinder2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MusicGenreComponent,
    MusicGenreDetailComponent,
    MusicGenreUpdateComponent,
    MusicGenreDeleteDialogComponent,
    MusicGenreDeletePopupComponent
  ],
  entryComponents: [MusicGenreComponent, MusicGenreUpdateComponent, MusicGenreDeleteDialogComponent, MusicGenreDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2MusicGenreModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
