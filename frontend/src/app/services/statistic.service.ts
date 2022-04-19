import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Statistic } from '../classes/statistic';
import { DatePipe } from '@angular/common'

@Injectable({
  providedIn: 'root'
})
export class StatisticService {

  constructor(public datepipe: DatePipe, private http: HttpClient) { }

  getAllCountries(): Observable<string[]> {
    return this.http.get<string[]>(environment.API_URL + '/countries');
  }

  getCountryIntervalHistory(country : string, initDate : Date, endDate : Date): Observable<Statistic[]> {
    let initial : string = this.datepipe.transform(initDate, 'dd-MM-yyyy')!;
    let end : string = this.datepipe.transform(endDate, 'dd-MM-yyyy')!;
    let params = new HttpParams();
    params = params.append('country', country);
    params = params.append('initial', initial);
    params = params.append('end', end);
    console.log(params)
    return this.http.get<Statistic[]>(environment.API_URL + '/interval_history', { params: params } );
  }

}
