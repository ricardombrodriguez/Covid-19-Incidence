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

  getAllCountriesStatistics(): Observable<Statistic[]> {
    return this.http.get<Statistic[]>(environment.API_URL + '/all_statistics');
  }

  getCountryStatistics(country : string): Observable<Statistic> {
    let params = new HttpParams();
    params = params.append('country', country);
    return this.http.get<Statistic>(environment.API_URL + '/statistics', { params: params } );
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

}
