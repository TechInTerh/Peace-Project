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
		let source = interval(5000); // every 5 seconds
		this.subscription = source.subscribe(val => {
			this.refresh_list()
		});
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

	refresh_list() {
		console.log("Refreshing list");
		this.reports = [];
		const post = this.backendService.sendReportRequest();
		post.subscribe(
			(data: any) => {
				data.forEach( (element: Object) => {
					this.reports.unshift(Report.fromJson(element))
				})
			}
		);
	}

	ngOnInit(): void {

	}

}
