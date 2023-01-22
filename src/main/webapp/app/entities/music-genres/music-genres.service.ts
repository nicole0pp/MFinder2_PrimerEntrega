import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMusicGenre } from 'app/shared/model/music-genres.model';

type EntityResponseType = HttpResponse<IMusicGenre>;
type EntityArrayResponseType = HttpResponse<IMusicGenre[]>;

@Injectable({ providedIn: 'root' })
export class MusicGenreService {
  public resourceUrl = SERVER_API_URL + 'api/music-genres';

  constructor(protected http: HttpClient) {}

  create(MusicGenre: IMusicGenre): Observable<EntityResponseType> {
    return this.http.post<IMusicGenre>(this.resourceUrl, MusicGenre, { observe: 'response' });
  }

  update(MusicGenre: IMusicGenre): Observable<EntityResponseType> {
    return this.http.put<IMusicGenre>(this.resourceUrl, MusicGenre, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMusicGenre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMusicGenre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
