import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SearchComponent } from './components/search/search.component';
import { StatisticComponent } from './components/statistic/statistic.component';
import { GraphComponent } from './components/graph/graph.component';
import { MapComponent } from './components/map/map.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { NewsComponent } from './components/news/news.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    StatisticComponent,
    GraphComponent,
    MapComponent,
    CalendarComponent,
    NewsComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
