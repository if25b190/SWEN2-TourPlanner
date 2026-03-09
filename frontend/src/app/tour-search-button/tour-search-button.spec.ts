import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourSearchButton } from './tour-search-button';

describe('TourSearchButton', () => {
  let component: TourSearchButton;
  let fixture: ComponentFixture<TourSearchButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourSearchButton]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourSearchButton);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
