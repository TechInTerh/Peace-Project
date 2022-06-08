import {Component} from '@angular/core';
import {BackendService} from "./services/backend.service";
import {interval, Subscription} from "rxjs";
import {DroneReportComponent} from "./component/drone-report/drone-report.component";
@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	title = 'srvc_website';
	logoSCPPath = "/assets/img/scp-logo.jpg";
	subscription: Subscription;
	reports: DroneReportComponent[] = [];
	constructor(private backendService: BackendService) {
		let source = interval(2000); // every 2 seconds
		this.subscription = source.subscribe(val => {this.refresh_list()});
	}
	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

	refresh_list(){
		console.log("Refreshing list");
	}

}
