import {Component, ElementRef, EventEmitter, Input, model, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {MapLocation, TourModel} from "../../model/m_tour";
import {TourService} from "../../service/tour";
import {ToastrService} from "ngx-toastr";
import {FormsModule} from "@angular/forms";
import {MapLocationPipe} from "../../pipe/map-location-pipe";

@Component({
    selector: 'app-tour-edit-modal',
    imports: [
        FormsModule,
        MapLocationPipe
    ],
    templateUrl: './tour-edit-modal.html',
    styleUrl: './tour-edit-modal.scss',
})
export class TourEditModal implements OnInit, OnDestroy {
    private selectMode?: "Start" | "Destination";
    private subscriptions: Subscription[] = [];
    readonly transportTypes = ["Bicycle", "Running", "Hiking"];
    tourName = model<string>('');
    description = model<string>('');
    start = model<MapLocation>();
    destination = model<MapLocation>();
    transportType = model<number>(0);
    @ViewChild("editTourModal") editTourModalRef: ElementRef<HTMLDialogElement> | undefined;
    @Input() tourData?: TourModel;
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
            this.editTourModalRef?.nativeElement.showModal();
            this.selectMode = undefined;
        });
        if (selectedLocationSub) {
            this.subscriptions.push(selectedLocationSub);
        }
        if (this.tourData) {
            this.setTourForm(this.tourData);
        }
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach((sub) => sub.unsubscribe());
    }

    selectStart(): void {
        this.selectMode = "Start";
        this.selectingEvent.emit();
        this.editTourModalRef?.nativeElement.close();
    }

    selectDestination(): void {
        this.selectMode = 'Destination';
        this.selectingEvent.emit();
        this.editTourModalRef?.nativeElement.close();
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

    editTour(): void {
        const tourModel: TourModel = {
            uuid: this.tourData?.uuid,
            name: this.tourName(),
            description: this.description(),
            from: this.start(),
            to: this.destination(),
            transportType: this.transportTypes[this.transportType()]
        }
        this.tourService.updateTour(tourModel, (tour) => {
            this.toastr.success("Tour updated!");
            this.editTourModalRef?.nativeElement.close();
            this.refreshData.emit();
        });
    }

    setTourForm(tour: TourModel): void {
        this.tourName.set(tour.name ?? "");
        this.description.set(tour.description ?? "");
        this.start.set(tour.from);
        this.destination.set(tour.to);
        const transportType = this.transportTypes.indexOf(tour.transportType ?? "");
        this.transportType.set(transportType != -1 ? transportType : 0);
    }
}
