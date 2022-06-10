export class Report {
	constructor(names: string[], lattitude: number, longitude: number) {
		this.names = names;
		this.lattitude = lattitude;
		this.longitude = longitude;

	}
	names: string[];
	lattitude: number;
	longitude: number;
	static fromJson(json: any): Report {
		return new Report(json.name, json.la, json.lon);
	}

}
