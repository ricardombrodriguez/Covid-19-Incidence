import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.css']
})
export class StatisticComponent implements OnInit {

  @Input() title! : string;
  @Input() value! : number | string;
  @Input() differential! : number;
  @Input() description! : string;

  constructor() { }

  ngOnInit(): void {

    if (this.value == null)
      this.value = "No Data";
      
  }

}
