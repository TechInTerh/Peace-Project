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

}
