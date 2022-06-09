import { Component, OnInit } from '@angular/core';
import {interval, Subscription} from "rxjs";
import {DroneReportComponent} from "../drone-report/drone-report.component";
import {BackendService} from "../../services/backend.service";
import {Report} from "../../Report/report";

@Component({
  selector: 'app-list-report',
  templateUrl: './list-report.component.html',
  styleUrls: ['./list-report.component.css']
})
export class ListReportComponent implements OnInit {
  subscription: Subscription;
  reports: Report[] = [];

  constructor(private backendService: BackendService) {
    let source = interval(2000); // every 2 seconds
    this.subscription = source.subscribe(val => {
      this.refresh_list()
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  refresh_list() {
    //this.reports.push(new Report(7,["March","Jean","Jeremy","Paul"],90.85,1.29));
    console.log("Refreshing list");
  }

  ngOnInit(): void {
    //create empty string list named list
    let list: string[] = [];

    this.reports.push(new Report(4,["Théo","Jean","Pierre","Paul"],85.85,2.29));
  }

}
