import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourModal } from './tour-modal';

describe('TourModal', () => {
  let component: TourModal;
  let fixture: ComponentFixture<TourModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
