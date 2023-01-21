import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAlbums } from 'app/shared/model/albums.model';

type EntityResponseType = HttpResponse<IAlbums>;
type EntityArrayResponseType = HttpResponse<IAlbums[]>;

@Injectable({ providedIn: 'root' })
export class AlbumsService {
  public resourceUrl = SERVER_API_URL + 'api/albums';

  constructor(protected http: HttpClient) {}

  create(albums: IAlbums): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(albums);
    return this.http
      .post<IAlbums>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(albums: IAlbums): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(albums);
    return this.http
      .put<IAlbums>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAlbums>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAlbums[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(albums: IAlbums): IAlbums {
    const copy: IAlbums = Object.assign({}, albums, {
      publicationDate:
        albums.publicationDate != null && albums.publicationDate.isValid() ? albums.publicationDate.format(DATE_FORMAT) : null
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
      res.body.forEach((albums: IAlbums) => {
        albums.publicationDate = albums.publicationDate != null ? moment(albums.publicationDate) : null;
      });
    }
    return res;
  }
}
