import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IListDetails } from 'app/shared/model/list-details.model';

type EntityResponseType = HttpResponse<IListDetails>;
type EntityArrayResponseType = HttpResponse<IListDetails[]>;

@Injectable({ providedIn: 'root' })
export class ListDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/list-details';

  constructor(protected http: HttpClient) {}

  create(listDetails: IListDetails): Observable<EntityResponseType> {
    return this.http.post<IListDetails>(this.resourceUrl, listDetails, { observe: 'response' });
  }

  update(listDetails: IListDetails): Observable<EntityResponseType> {
    return this.http.put<IListDetails>(this.resourceUrl, listDetails, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IListDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IListDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
