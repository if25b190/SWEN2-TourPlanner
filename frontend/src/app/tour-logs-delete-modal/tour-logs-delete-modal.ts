import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {TourLogModel} from "../../model/m_tourlog";
import {ToastrService} from "ngx-toastr";
import {TourLogService} from "../../service/tour_log";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-tour-logs-delete-modal',
  imports: [
    FormsModule
  ],
  templateUrl: './tour-logs-delete-modal.html',
  styleUrl: './tour-logs-delete-modal.scss',
})
export class TourLogsDeleteModal {
  @ViewChild("deleteTourLogModal") deleteTourLogModal: ElementRef<HTMLDialogElement> | undefined;
  @Input({ required: true }) tourLogData?: TourLogModel;
  @Output() refreshData = new EventEmitter<void>();

  constructor(private tourLogService: TourLogService, private toastr: ToastrService) {
  }

  deleteTourLog(): void {
    if (!this.tourLogData?.uuid) {
      this.toastr.error("UUID is missing!");
      return;
    }
    this.tourLogService.deleteTourLog(this.tourLogData.uuid, () => {
      this.toastr.success("Tour log deleted!");
      this.deleteTourLogModal?.nativeElement.close();
      this.refreshData.emit();
    });
  }
}
