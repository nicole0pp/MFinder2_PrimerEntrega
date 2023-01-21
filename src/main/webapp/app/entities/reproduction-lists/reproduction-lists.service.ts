import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReproductionLists } from 'app/shared/model/reproduction-lists.model';

type EntityResponseType = HttpResponse<IReproductionLists>;
type EntityArrayResponseType = HttpResponse<IReproductionLists[]>;

@Injectable({ providedIn: 'root' })
export class ReproductionListsService {
  public resourceUrl = SERVER_API_URL + 'api/reproduction-lists';

  constructor(protected http: HttpClient) {}

  create(reproductionLists: IReproductionLists): Observable<EntityResponseType> {
    return this.http.post<IReproductionLists>(this.resourceUrl, reproductionLists, { observe: 'response' });
  }

  update(reproductionLists: IReproductionLists): Observable<EntityResponseType> {
    return this.http.put<IReproductionLists>(this.resourceUrl, reproductionLists, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReproductionLists>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReproductionLists[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
