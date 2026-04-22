import {Component, inject, Input, OnInit, signal, WritableSignal} from '@angular/core';
import {TourLogService} from "../../service/tour_log";
import {TourLogModel} from "../../model/m_tourlog";
import {TourLogsEditModal} from "../tour-logs-edit-modal/tour-logs-edit-modal";
import {TourLogsDeleteModal} from "../tour-logs-delete-modal/tour-logs-delete-modal";
import {TourLogsAddModal} from "../tour-logs-add-modal/tour-logs-add-modal";

@Component({
    selector: 'app-tour-logs',
    imports: [
        TourLogsEditModal,
        TourLogsDeleteModal,
        TourLogsAddModal
    ],
    templateUrl: './tour-logs.html',
    styleUrl: './tour-logs.scss',
})
export class TourLogs implements OnInit {
    private readonly tourLogsService = inject(TourLogService);
    @Input({required: true}) tourUuid!: string;
    tourLogs: WritableSignal<TourLogModel[]> = signal([]);

    ngOnInit(): void {
        this.fetchAllTourLogs();
    }

    fetchAllTourLogs(): void {
        this.tourLogsService.fetchAllTourLogs(this.tourUuid, (tourLogs: TourLogModel[]) => {
            this.tourLogs.set(tourLogs);
        });
    }
}
