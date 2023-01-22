import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFavoriteList } from 'app/shared/model/reproduction-lists.model';

type EntityResponseType = HttpResponse<IFavoriteList>;
type EntityArrayResponseType = HttpResponse<IFavoriteList[]>;

@Injectable({ providedIn: 'root' })
export class FavoriteListService {
  public resourceUrl = SERVER_API_URL + 'api/reproduction-lists';

  constructor(protected http: HttpClient) {}

  create(FavoriteList: IFavoriteList): Observable<EntityResponseType> {
    return this.http.post<IFavoriteList>(this.resourceUrl, FavoriteList, { observe: 'response' });
  }

  update(FavoriteList: IFavoriteList): Observable<EntityResponseType> {
    return this.http.put<IFavoriteList>(this.resourceUrl, FavoriteList, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFavoriteList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFavoriteList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
