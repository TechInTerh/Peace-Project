import {Component, OnInit} from '@angular/core';
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

		if (this.reports.length < 2) {
			this.reports.push(new Report(["March", "Jean", "Jeremy", "Paul"], 90.85, 1.29));
		}
		const post = this.backendService.sendReportRequest();
		//clea reports
		this.reports = [];
		post.subscribe(
			(data: any) => {
				data.forEach( (element: Object) => {
					this.reports.push(Report.fromJson(element));
					console.log(element);
				})
			}
		);
		console.log("Refreshing list");
	}

	ngOnInit(): void {

	}

}
