export class Report {
	constructor(id:number, names: string[],lattitude: number, longitude: number) {
		this.id = id;
		this.names = names;
		this.lattitude = lattitude;
		this.longitude = longitude;

	}
	id:number;
	names: string[];
	lattitude: number;
	longitude: number;

}
