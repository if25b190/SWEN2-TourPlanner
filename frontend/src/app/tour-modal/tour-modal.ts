import {Component, EventEmitter, Input, model, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {TourService} from "../../service/tour";

@Component({
  selector: 'app-tour-modal',
    imports: [
        FormsModule
    ],
  templateUrl: './tour-modal.html',
  styleUrl: './tour-modal.scss',
})
export class TourModal {
  private selectMode?: "Start" | "Destination";
  readonly transportTypes = ["Bicycle", "Running", "Hiking"];
  tourName = model<String>('');
  description = model<String>('');
  start = model<String>('');
  destination = model<String>('');
  transportType = model<number>(0);
  @Input() selectedLocation?: {latitude: number, longitude: number};
  @Output() selectingEvent = new EventEmitter<boolean>();

  constructor() {
  }

  selectStart() {
    this.selectMode = "Start";
    this.selectingEvent.emit(true);
  }

  selectDestination() {
    this.selectMode = 'Destination';
    this.selectingEvent.emit(true);
  }

  isTourNameValid(): boolean {
    return this.tourName().length >= 4;
  }

  isDescriptionValid(): boolean {
    return this.description().length >= 4;
  }
}
