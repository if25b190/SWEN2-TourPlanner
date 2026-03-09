import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourDeleteModal } from './tour-delete-modal';

describe('TourDeleteModal', () => {
  let component: TourDeleteModal;
  let fixture: ComponentFixture<TourDeleteModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourDeleteModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourDeleteModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
