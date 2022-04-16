import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SearchComponent } from './components/search/search.component';
import { StatisticComponent } from './components/statistic/statistic.component';
import { GraphComponent } from './components/graph/graph.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { NewsComponent } from './components/news/news.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DistributionComponent } from './components/distribution/distribution.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    StatisticComponent,
    GraphComponent,
    CalendarComponent,
    NewsComponent,
    DashboardComponent,
    DistributionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
