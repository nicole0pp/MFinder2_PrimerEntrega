import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'Song',
        loadChildren: './Song/Song.module#MFinder2SongModule'
      },
      {
        path: 'Album',
        loadChildren: './Album/Album.module#MFinder2AlbumModule'
      },
      {
        path: 'reproduction-lists',
        loadChildren: './reproduction-lists/reproduction-lists.module#MFinder2FavoriteListModule'
      },
      {
        path: 'list-details',
        loadChildren: './list-details/list-details.module#MFinder2ListDetailsModule'
      },
      {
        path: 'music-genres',
        loadChildren: './music-genres/music-genres.module#MFinder2MusicGenreModule'
      },
      {
        path: 'artist',
        loadChildren: './artist/artist.module#MFinder2ArtistModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MFinder2EntityModule {}
