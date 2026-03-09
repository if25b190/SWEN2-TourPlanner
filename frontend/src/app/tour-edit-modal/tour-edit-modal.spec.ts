import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourEditModal } from './tour-edit-modal';

describe('TourEditModal', () => {
  let component: TourEditModal;
  let fixture: ComponentFixture<TourEditModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourEditModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourEditModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
