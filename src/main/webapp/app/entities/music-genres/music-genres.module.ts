import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MFinder2SharedModule } from 'app/shared';
import {
  MusicGenresComponent,
  MusicGenresDetailComponent,
  MusicGenresUpdateComponent,
  MusicGenresDeletePopupComponent,
  MusicGenresDeleteDialogComponent,
  musicGenresRoute,
  musicGenresPopupRoute
} from './';

const ENTITY_STATES = [...musicGenresRoute, ...musicGenresPopupRoute];

@NgModule({
  imports: [MFinder2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MusicGenresComponent,
    MusicGenresDetailComponent,
    MusicGenresUpdateComponent,
    MusicGenresDeleteDialogComponent,
    MusicGenresDeletePopupComponent
  ],
  entryComponents: [MusicGenresComponent, MusicGenresUpdateComponent, MusicGenresDeleteDialogComponent, MusicGenresDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2MusicGenresModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
