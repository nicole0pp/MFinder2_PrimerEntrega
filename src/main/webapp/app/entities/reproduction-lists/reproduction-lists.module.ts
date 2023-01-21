import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MFinder2SharedModule } from 'app/shared';
import {
  ReproductionListsComponent,
  ReproductionListsDetailComponent,
  ReproductionListsUpdateComponent,
  ReproductionListsDeletePopupComponent,
  ReproductionListsDeleteDialogComponent,
  reproductionListsRoute,
  reproductionListsPopupRoute
} from './';

const ENTITY_STATES = [...reproductionListsRoute, ...reproductionListsPopupRoute];

@NgModule({
  imports: [MFinder2SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ReproductionListsComponent,
    ReproductionListsDetailComponent,
    ReproductionListsUpdateComponent,
    ReproductionListsDeleteDialogComponent,
    ReproductionListsDeletePopupComponent
  ],
  entryComponents: [
    ReproductionListsComponent,
    ReproductionListsUpdateComponent,
    ReproductionListsDeleteDialogComponent,
    ReproductionListsDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2ReproductionListsModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
