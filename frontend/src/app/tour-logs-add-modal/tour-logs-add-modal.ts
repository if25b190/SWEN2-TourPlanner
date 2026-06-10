import {Component, ElementRef, EventEmitter, Input, model, Output, ViewChild} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {FormsModule} from "@angular/forms";
import {Difficulty, TourLogModel, TourLogRating, TourLogRatings} from "../../model/m_tourlog";
import {TourLogService} from "../../service/tour_log";

@Component({
    selector: 'app-tour-logs-add-modal',
    imports: [
        FormsModule
    ],
    templateUrl: './tour-logs-add-modal.html',
    styleUrl: './tour-logs-add-modal.scss',
})
export class TourLogsAddModal {
    readonly difficulties: string[] = [];
    readonly ratings = TourLogRatings;
    creationDate = model<Date>();
    totalTime = model<number>();
    comment = model<string>("");
    distance = model<number>(0);
    difficulty = model<number>(0);
    rating = model<TourLogRating>();
    @ViewChild("addTourLogModal") addTourLogModalRef: ElementRef<HTMLDialogElement> | undefined;
    @Input({ required: true }) tourUuid!: string;
    @Output() refreshData = new EventEmitter<void>();

    constructor(private tourLogService: TourLogService, private toastr: ToastrService) {
        for (const diff in Difficulty) {
            this.difficulties.push(diff);
        }
    }

    isCreationDateValid(): boolean {
        return this.creationDate() != undefined;
    }

    isTotalTimeValid(): boolean {
        const time = this.totalTime();
        return time != undefined && Number.isInteger(time) && time > 0;
    }

    isDistanceValid(): boolean {
        return this.distance() != undefined;
    }

    isRatingValid(): boolean {
        return this.rating() != undefined;
    }

    isFormValid(): boolean {
        return this.isCreationDateValid() && this.isTotalTimeValid() && this.isDistanceValid() && this.isRatingValid();
    }

    createTourLog(): void {
        const logModel: TourLogModel = {
            tour: this.tourUuid,
            creationDate: this.creationDate(),
            totalTime: this.totalTime(),
            comment: this.comment(),
            distance: this.distance(),
            difficulty: this.difficulties[this.difficulty()],
            rating: this.rating()
        }
        this.tourLogService.createTourLog(logModel).subscribe({
            next: () => {
                this.toastr.success("Tour log added!");
                this.clearTourLogForm();
                this.addTourLogModalRef?.nativeElement.close();
                this.refreshData.emit();
            },
            error: (err) => {
                console.error(err);
                this.toastr.error("Failed to create tourlog!");
            }
        });
    }

    clearTourLogForm(): void {
        this.creationDate.set(undefined);
        this.totalTime.set(undefined);
        this.comment.set("");
        this.distance.set(0);
        this.difficulty.set(0);
        this.rating.set(undefined);
    }
}
