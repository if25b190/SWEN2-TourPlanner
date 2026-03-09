import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TourModel} from "../../model/m_tour";
import {TourService} from "../../service/tour";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-tour-delete-modal',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './tour-delete-modal.html',
  styleUrl: './tour-delete-modal.scss',
})
export class TourDeleteModal {
  @ViewChild("deleteTourModal") deleteTourModal: ElementRef<HTMLDialogElement> | undefined;
  @Input() tourData?: TourModel;
  @Output() refreshData = new EventEmitter<void>();

  constructor(private tourService: TourService, private toastr: ToastrService) {
  }

  deleteTour(): void {
    if (!this.tourData?.uuid) {
      this.toastr.error("UUID is missing!");
      return;
    }
    this.tourService.deleteTour(this.tourData.uuid, () => {
      this.toastr.success("Tour deleted!");
      this.deleteTourModal?.nativeElement.close();
      this.refreshData.emit();
    });
  }
}
