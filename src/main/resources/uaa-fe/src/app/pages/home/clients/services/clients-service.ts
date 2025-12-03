import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {PageRequest, PageT} from '../../../common/BaseTable';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ClientsService {

  constructor(private httpClient: HttpClient) {
  }

  list(request: PageRequest): Observable<PageT<ClientSummary>> {
    return this.httpClient.get<PageT<ClientSummary>>("api/v1/admin/clients", {params: this.getParams(request)});
  }

  delete(clientId: string): Observable<any> {
    return this.httpClient.delete(`api/v1/admin/clients/${clientId}`);
  }

  findOne(clientId: string): Observable<any> {
    return this.httpClient.get(`api/v1/admin/clients/${clientId}`);
  }

  getParams(request: PageRequest): HttpParams {
    return new HttpParams({fromObject: {...request}});
  }

  save(value: any) {
    return this.httpClient.post(`api/v1/admin/clients`,value);
  }
}

export interface ClientSummary {
  id: string
  clientId: string
  clientName: string
}
