import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Statistic } from 'src/app/classes/statistic';
import { StatisticService } from 'src/app/services/statistic.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  country! : string;
  statistics! : Statistic[];
  search ! : FormGroup;
  new_cases_diff ! : number;

  constructor(private statisticService : StatisticService, private fb : FormBuilder) { }

  ngOnInit(): void {

  }

  getCountryHistory(): void {
    console.log("Country history:")
    this.statisticService.getCountryHistory(this.country).subscribe((statistics) => {
      this.statistics = statistics;
      console.log(this.statistics);
      console.log(this.statistics[0])
    });
  }

}
