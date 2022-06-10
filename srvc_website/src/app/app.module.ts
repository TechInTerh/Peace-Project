import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DroneReportComponent } from './component/drone-report/drone-report.component';
import { ListReportComponent } from './component/list-report/list-report.component';
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import {IgxListModule, IgxRippleModule} from "igniteui-angular";

@NgModule({
	declarations: [
		AppComponent,
		DroneReportComponent,
		ListReportComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		HttpClientModule,
		BrowserAnimationsModule,
		IgxListModule,
		IgxRippleModule
	],
	providers: [],
	bootstrap: [AppComponent]
})
export class AppModule {
}
