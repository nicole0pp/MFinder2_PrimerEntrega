import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAlbum } from 'app/shared/model/Album.model';

type EntityResponseType = HttpResponse<IAlbum>;
type EntityArrayResponseType = HttpResponse<IAlbum[]>;

@Injectable({ providedIn: 'root' })
export class AlbumService {
  public resourceUrl = SERVER_API_URL + 'api/Album';

  constructor(protected http: HttpClient) {}

  create(Album: IAlbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(Album);
    return this.http
      .post<IAlbum>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(Album: IAlbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(Album);
    return this.http
      .put<IAlbum>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAlbum>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAlbum[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(Album: IAlbum): IAlbum {
    const copy: IAlbum = Object.assign({}, Album, {
      publicationDate: Album.publicationDate != null && Album.publicationDate.isValid() ? Album.publicationDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.publicationDate = res.body.publicationDate != null ? moment(res.body.publicationDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((Album: IAlbum) => {
        Album.publicationDate = Album.publicationDate != null ? moment(Album.publicationDate) : null;
      });
    }
    return res;
  }
}
