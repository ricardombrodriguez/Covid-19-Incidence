import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RequestStat } from '../classes/request';

@Injectable({
  providedIn: 'root'
})
export class CacheService {

  constructor(private http: HttpClient) { }

  getCache(): Observable<RequestStat[]> {
    return this.http.get<RequestStat[]>('http://localhost:8080/cache');
  }
}
