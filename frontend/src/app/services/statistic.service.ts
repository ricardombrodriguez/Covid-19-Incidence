import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Statistic } from '../classes/statistic';
import { DatePipe } from '@angular/common'
import { RequestStat } from '../classes/request';

@Injectable({
  providedIn: 'root'
})
export class StatisticService {

  constructor(public datepipe: DatePipe, private http: HttpClient) { }

  getAllCountries(): Observable<String[]> {
    return this.http.get<String[]>('http://localhost:8080/countries');
  }

  getCountryIntervalHistory(country : string, initDate : Date, endDate : Date): Observable<RequestStat> {
    let initial : string = this.datepipe.transform(initDate, 'dd-MM-yyyy')!;
    let end : string = this.datepipe.transform(endDate, 'dd-MM-yyyy')!;
    let params = new HttpParams();
    params = params.append('country', country);
    params = params.append('initial', initial);
    params = params.append('end', end);
    return this.http.get<RequestStat>('http://localhost:8080/interval_history', { params: params } );
  }

}
