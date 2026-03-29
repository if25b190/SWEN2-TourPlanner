import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourLogsEditModal } from './tour-logs-edit-modal';

describe('TourLogsEditModal', () => {
  let component: TourLogsEditModal;
  let fixture: ComponentFixture<TourLogsEditModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourLogsEditModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourLogsEditModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
