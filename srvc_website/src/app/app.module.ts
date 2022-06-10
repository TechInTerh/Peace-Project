import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DroneReportComponent } from './component/drone-report/drone-report.component';
import { ListReportComponent } from './component/list-report/list-report.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    DroneReportComponent,
    ListReportComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
      HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
