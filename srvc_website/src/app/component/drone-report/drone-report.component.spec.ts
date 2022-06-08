import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DroneReportComponent } from './drone-report.component';

describe('DroneReportComponent', () => {
  let component: DroneReportComponent;
  let fixture: ComponentFixture<DroneReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DroneReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DroneReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
