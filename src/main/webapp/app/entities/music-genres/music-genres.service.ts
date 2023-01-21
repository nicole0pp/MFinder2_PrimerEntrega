import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMusicGenres } from 'app/shared/model/music-genres.model';

type EntityResponseType = HttpResponse<IMusicGenres>;
type EntityArrayResponseType = HttpResponse<IMusicGenres[]>;

@Injectable({ providedIn: 'root' })
export class MusicGenresService {
  public resourceUrl = SERVER_API_URL + 'api/music-genres';

  constructor(protected http: HttpClient) {}

  create(musicGenres: IMusicGenres): Observable<EntityResponseType> {
    return this.http.post<IMusicGenres>(this.resourceUrl, musicGenres, { observe: 'response' });
  }

  update(musicGenres: IMusicGenres): Observable<EntityResponseType> {
    return this.http.put<IMusicGenres>(this.resourceUrl, musicGenres, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMusicGenres>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMusicGenres[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
