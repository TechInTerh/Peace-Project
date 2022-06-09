import { Component, OnInit, Input } from '@angular/core';
import {Report} from "../../Report/report";

@Component({
  selector: 'app-drone-report',
  templateUrl: './drone-report.component.html',
  styleUrls: ['./drone-report.component.css']
})
export class DroneReportComponent implements OnInit {

  constructor() { }

  @Input() reports!: Report;
  ngOnInit(): void {
  }

}
