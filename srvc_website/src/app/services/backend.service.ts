import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {retry} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BackendService {
	SERVER_URL = "http://localhost:8080/alerts";
  constructor(private http: HttpClient) { }

	sendReportRequest() {
	  return this.http.get(this.SERVER_URL).pipe(retry(3));
	}
}
