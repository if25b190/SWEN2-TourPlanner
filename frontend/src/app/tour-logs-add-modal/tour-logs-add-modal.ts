import {Component, ElementRef, EventEmitter, Input, model, Output, ViewChild} from '@angular/core';
import {TourModel} from "../../model/m_tour";
import {ToastrService} from "ngx-toastr";
import {FormsModule} from "@angular/forms";
import {Difficulty, TourLogModel} from "../../model/m_tourlog";
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
    creationDate = model<Date>();
    totalTime = model<Date>();
    comment = model<string>("");
    distance = model<number>(0);
    difficulty = model<number>(0);
    rating = model<number>();
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
        this.tourLogService.createTourLog(logModel, (tourLog) => {
            this.toastr.success("Tour log added!");
            this.clearTourLogForm();
            this.addTourLogModalRef?.nativeElement.close();
            this.refreshData.emit();
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