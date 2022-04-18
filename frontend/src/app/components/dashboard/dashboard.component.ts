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

  options: any;
  country! : string;
  statistics! : Statistic[];
  search! : FormGroup;
  newCasesDifferential! : number;
  activeCasesDifferential! : number;
  newDeathsDifferential! : number;
  recoveredDifferential! : number;
  newCriticalDifferential! : number;
  totalCasesDifferential! : number;
  totalTestsDifferential! : number;
  totalDeathsDifferential! : number;
  radioValue : number = 0;
  fetchDays! : number;
  countries : string[] = [];

  constructor(private statisticService : StatisticService, private fb : FormBuilder) { }

  ngOnInit(): void {

    this.search = this.fb.group({
      option:[0]
    });

    this.getAllCountries();

  }

  getAllCountries() : void {

    this.statisticService.getAllCountries().subscribe((countries) => {
      this.countries = countries;
    });

  }

  getCountryHistory(): void {
    this.statisticService.getCountryHistory(this.country).subscribe((statistics) => {

      this.fetchDays = this.search.get('option')?.value;
      this.statistics = statistics;

      let dateOffset = 24*60*60*1000;
      let initialDate : Date = new Date(this.statistics[0].time);
      let endDate : Date = new Date(this.statistics[0].time);
      let calculatedDays = (this.fetchDays == 0 ? 365 : this.fetchDays)
      console.log(calculatedDays)
      initialDate.setTime(initialDate.getTime() - dateOffset * calculatedDays);
      console.log(initialDate)
      this.calculateDifferentials(initialDate, endDate);

      const xAxisData = [];
      const newCases = [];
      const newCritical = [];
      const newDeaths = [];

      // Exclude information with 1+ year
      for (let i = this.statistics.length - 1; i >= 0; i--) {

        let stat : Statistic = this.statistics[i];
        let graphDays = (this.fetchDays > 0) ? this.fetchDays : 365;

        // 1 year difference
        if (Math.abs((new Date(stat.time).getTime() - new Date(statistics[0].time).getTime()) / 1000 / 60 / 60 / 24) > graphDays) {
          continue
        }

        xAxisData.push(stat.time);
        newCases.push(stat.newCases);
        newCritical.push(stat.newCritical);
        newDeaths.push(stat.newDeaths);
        
      }

      this.options = {
        legend: {
          data: ['New cases', 'New critical', 'New deaths'],
          align: 'right',
        },
        tooltip: {},
        xAxis: {
          data: xAxisData,
          silent: false,
          splitLine: {
            show: false,
          },
        },
        yAxis: {},
        series: [
          {
            name: 'New cases',
            type: 'bar',
            data: newCases,
            animationDelay: (idx: number) => idx * 10,
          },
          {
            name: 'New critical',
            type: 'bar',
            data: newCritical,
            animationDelay: (idx: number) => idx * 10 + 100,
          },
          {
            name: 'New deaths',
            type: 'bar',
            data: newDeaths,
            animationDelay: (idx: number) => idx * 10 + 200,
          },
        ],
        animationEasing: 'elasticOut',
        animationDelayUpdate: (idx: number) => idx * 5,
      };

    });
  }

  calculateDifferentials(initial : Date, end : Date) : void {

    let initialStat : Statistic = new Statistic();
    let endStat : Statistic = new Statistic();

    for (let i = this.statistics.length - 1; i >= 0; i--) {
      let statDate : Date = new Date(this.statistics[i].time);
      if (statDate.valueOf() === initial.valueOf()) {
        initialStat = this.statistics[i];
      }
      if (statDate.valueOf() === end.valueOf()) { 
        endStat = this.statistics[i];
      }
    }

    this.newCasesDifferential = this.calculateGrowthRate(initialStat.newCases,endStat.newCases);
    this.activeCasesDifferential = this.calculateGrowthRate(initialStat.active,endStat.active);
    this.recoveredDifferential = this.calculateGrowthRate(initialStat.recovered,endStat.recovered);
    this.newDeathsDifferential = this.calculateGrowthRate(initialStat.newDeaths,endStat.newDeaths);
    this.totalCasesDifferential = this.calculateGrowthRate(initialStat.totalCases,endStat.totalCases);
    this.newCriticalDifferential = this.calculateGrowthRate(initialStat.newCritical,endStat.newCritical);
    this.totalTestsDifferential = this.calculateGrowthRate(initialStat.totalTests,endStat.totalTests);
    this.totalDeathsDifferential = this.calculateGrowthRate(initialStat.totalDeaths,endStat.totalDeaths);

  }

  calculateGrowthRate(oldValue : number, newValue : number) : number {
    return ( (newValue - oldValue) / oldValue) * 100;
  }

}