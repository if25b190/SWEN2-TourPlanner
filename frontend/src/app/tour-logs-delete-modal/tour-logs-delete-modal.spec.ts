import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourLogsDeleteModal } from './tour-logs-delete-modal';

describe('TourLogsDeleteModal', () => {
  let component: TourLogsDeleteModal;
  let fixture: ComponentFixture<TourLogsDeleteModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourLogsDeleteModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourLogsDeleteModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
