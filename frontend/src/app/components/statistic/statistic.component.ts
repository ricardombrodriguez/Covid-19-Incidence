import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.css']
})
export class StatisticComponent implements OnInit {

  @Input() title! : string;
  @Input() value! : number;
  @Input() differential! : number;
  @Input() description! : string;
  @Input() icon! : string;

  constructor() { }

  ngOnInit(): void {
  }

}
