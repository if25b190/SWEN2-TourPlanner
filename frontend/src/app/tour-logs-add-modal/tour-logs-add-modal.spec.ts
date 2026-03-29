import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourLogsAddModal } from './tour-logs-add-modal';

describe('TourLogsAddModal', () => {
  let component: TourLogsAddModal;
  let fixture: ComponentFixture<TourLogsAddModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourLogsAddModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourLogsAddModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
