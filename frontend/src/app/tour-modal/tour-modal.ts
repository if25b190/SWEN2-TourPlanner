import {Component, ElementRef, EventEmitter, Input, model, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {TourService} from "../../service/tour";
import {Observable, Subscription} from "rxjs";
import {MapLocation, TourModel} from "../../model/m_tour";
import {ToastrService} from "ngx-toastr";
import {MapLocationPipe} from "../../pipe/map-location-pipe";

@Component({
  selector: 'app-tour-modal',
  imports: [
    FormsModule,
    MapLocationPipe
  ],
  templateUrl: './tour-modal.html',
  styleUrl: './tour-modal.scss',
})
export class TourModal implements OnInit, OnDestroy {
  private selectMode?: "Start" | "Destination";
  private subscriptions: Subscription[] = [];
  readonly transportTypes = ["Bicycle", "Running", "Hiking"];
  tourName = model<string>('');
  description = model<string>('');
  start = model<MapLocation>();
  destination = model<MapLocation>();
  transportType = model<number>(0);
  @ViewChild("addTourModal") addTourModalRef: ElementRef<HTMLDialogElement> | undefined;
  @Input() selectedLocation?: Observable<MapLocation>;
  @Output() selectingEvent = new EventEmitter<void>();
  @Output() refreshData = new EventEmitter<void>();

  constructor(private tourService: TourService, private toastr: ToastrService) {
  }

  ngOnInit(): void {
    const selectedLocationSub = this.selectedLocation?.subscribe((mapLocation) => {
      if (!this.selectMode) {
        return;
      }
      switch (this.selectMode) {
        case "Start":
          this.start.set(mapLocation);
          break;
        case "Destination":
          this.destination.set(mapLocation);
          break;
      }
      this.addTourModalRef?.nativeElement.showModal();
      this.selectMode = undefined;
    });
    if (selectedLocationSub) {
      this.subscriptions.push(selectedLocationSub);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

  selectStart(): void {
    this.selectMode = "Start";
    this.selectingEvent.emit();
    this.addTourModalRef?.nativeElement.close();
  }

  selectDestination(): void {
    this.selectMode = 'Destination';
    this.selectingEvent.emit();
    this.addTourModalRef?.nativeElement.close();
  }

  isTourNameValid(): boolean {
    return this.tourName().length >= 4;
  }

  isDescriptionValid(): boolean {
    return this.description().length >= 4;
  }

  isStartValid(): boolean {
    return this.start() != undefined;
  }

  isDestinationValid(): boolean {
    return this.destination() != undefined;
  }

  isFormValid(): boolean {
    return this.isTourNameValid() &&
        this.isDescriptionValid() &&
        this.isStartValid() &&
        this.isDestinationValid();
  }

  createTour(): void {
    const tourModel: TourModel = {
      name: this.tourName(),
      description: this.description(),
      from: this.start(),
      to: this.destination(),
      transportType: this.transportTypes[this.transportType()]
    }
    this.tourService.createTour(tourModel, (tour) => {
      this.toastr.success("Tour added!");
      this.clearTourForm();
      this.addTourModalRef?.nativeElement.close();
      this.refreshData.emit();
    });
  }

  clearTourForm(): void {
    this.tourName.set("");
    this.description.set("");
    this.start.set(undefined);
    this.destination.set(undefined);
    this.transportType.set(0);
  }
}
