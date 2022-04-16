import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Statistic } from '../classes/statistic';
@Injectable({
  providedIn: 'root'
})
export class StatisticService {

  constructor(private http: HttpClient) { }

  getAllCountries(): Observable<string[]> {
    return this.http.get<string[]>(environment.API_URL + '/countries');
  }

  getCountryHistoryByDate(country : string, day : string): Observable<Statistic[]> {
    let params = new HttpParams();
    params = params.append('country', country);
    params = params.append('day', day);
    return this.http.get<Statistic[]>(environment.API_URL + '/day_history', { params: params } );
  }

  getCountryHistory(country : string): Observable<Statistic[]> {
    let params = new HttpParams();
    params = params.append('country', country);
    return this.http.get<Statistic[]>(environment.API_URL + '/history', { params: params } );
  }

  getCountryIntervalHistory(country : string, initial : string, end : string): Observable<Statistic[]> {
    let params = new HttpParams();
    params = params.append('country', country);
    params = params.append('initial', initial);
    params = params.append('end', end);
    return this.http.get<Statistic[]>(environment.API_URL + '/interval_history', { params: params } );
  }

}
