import {Component, inject, Input, OnInit} from '@angular/core';
import {TourLogService} from "../../service/tour_log";
import {TourLogModel} from "../../model/m_tourlog";
import {TourLogsEditModal} from "../tour-logs-edit-modal/tour-logs-edit-modal";
import {TourLogsDeleteModal} from "../tour-logs-delete-modal/tour-logs-delete-modal";

@Component({
  selector: 'app-tour-logs',
  imports: [
    TourLogsEditModal,
    TourLogsDeleteModal
  ],
  templateUrl: './tour-logs.html',
  styleUrl: './tour-logs.scss',
})
export class TourLogs implements OnInit {
  private readonly tourLogsService = inject(TourLogService);
  @Input({ required: true }) tourUuid!: string;
  tourLogs: TourLogModel[] = [];

  ngOnInit(): void {
    this.fetchAllTourLogs();
  }

  fetchAllTourLogs(): void {
    this.tourLogsService.fetchAllTourLogs(this.tourUuid, (tourLogs: TourLogModel[]) => {
      this.tourLogs = tourLogs;
    });
  }
}
