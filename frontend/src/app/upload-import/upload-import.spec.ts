import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadImport } from './upload-import';

describe('UploadImport', () => {
  let component: UploadImport;
  let fixture: ComponentFixture<UploadImport>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UploadImport]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UploadImport);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
