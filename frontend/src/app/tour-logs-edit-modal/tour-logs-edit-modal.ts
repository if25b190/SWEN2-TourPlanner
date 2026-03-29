import {Component, ElementRef, EventEmitter, Input, model, OnInit, Output, ViewChild} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {FormsModule} from "@angular/forms";
import {Difficulty, TourLogModel} from "../../model/m_tourlog";
import {TourLogService} from "../../service/tour_log";

@Component({
  selector: 'app-tour-logs-edit-modal',
  imports: [
    FormsModule
  ],
  templateUrl: './tour-logs-edit-modal.html',
  styleUrl: './tour-logs-edit-modal.scss',
})
export class TourLogsEditModal implements OnInit {
  readonly difficulties: string[] = [];
  creationDate = model<Date>();
  totalTime = model<Date>();
  comment = model<string>("");
  distance = model<number>(0);
  difficulty = model<number>(0);
  rating = model<number>();
  @ViewChild("editTourLogModal") editTourLogModalRef: ElementRef<HTMLDialogElement> | undefined;
  @Input({required: true}) tourLogData?: TourLogModel;
  @Output() refreshData = new EventEmitter<void>();

  constructor(private tourLogService: TourLogService, private toastr: ToastrService) {
    for (const diff in Difficulty) {
      this.difficulties.push(diff);
    }
  }

  ngOnInit(): void {
    if (this.tourLogData) {
      this.setTourLogForm(this.tourLogData);
    }
  }

  setTourLogForm(tourLog: TourLogModel): void {
    this.creationDate.set(tourLog.creationDate);
    this.totalTime.set(tourLog.totalTime);
    this.comment.set(tourLog.comment ?? "");
    this.distance.set(tourLog.distance ?? 0);
    this.rating.set(tourLog.rating)
    const diffIndex = this.difficulties.indexOf(tourLog.difficulty ?? "");
    this.difficulty.set(diffIndex !== -1 ? diffIndex : 0);
  }

  isCreationDateValid(): boolean {
    return this.creationDate() != undefined;
  }

  isTotalTimeValid(): boolean {
    return this.totalTime() != undefined;
  }

  isDistanceValid(): boolean {
    return this.distance() != undefined;
  }

  isRatingValid(): boolean {
    const r = this.rating();
    return r != undefined && r >= 1 && r <= 5;
  }

  isFormValid(): boolean {
    return this.isCreationDateValid() && this.isTotalTimeValid() && this.isDistanceValid() && this.isRatingValid();
  }

  editTourLog(): void {
    const logModel: TourLogModel = {
      uuid: this.tourLogData!.uuid,
      tour: this.tourLogData!.tour,
      creationDate: this.creationDate(),
      totalTime: this.totalTime(),
      comment: this.comment(),
      distance: this.distance(),
      difficulty: this.difficulties[this.difficulty()],
      rating: this.rating()
    }
    this.tourLogService.updateTourLog(logModel, () => {
      this.toastr.success("Tour log updated!");
      this.editTourLogModalRef?.nativeElement.close();
      this.refreshData.emit();
    });
  }
}