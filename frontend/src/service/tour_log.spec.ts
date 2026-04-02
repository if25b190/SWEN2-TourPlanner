import { TestBed } from '@angular/core/testing';

import {TourLogService} from "./tour_log";

describe('Tourlog', () => {
  let service: TourLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TourLogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
