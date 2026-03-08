import {AfterViewInit, Component} from '@angular/core';
import {TourService} from "../../service/tour";
import {FormsModule} from "@angular/forms";
import * as leaflet from "leaflet";
import {TourModal} from "../tour-modal/tour-modal";
import {Subject} from "rxjs";
import {TourModel} from "../../model/m_tour";
import {TourEditModal} from "../tour-edit-modal/tour-edit-modal";
import {TourDeleteModal} from "../tour-delete-modal/tour-delete-modal";

@Component({
    selector: 'app-tours',
    imports: [
        FormsModule,
        TourModal,
        TourEditModal,
        TourDeleteModal
    ],
    templateUrl: './tours.html',
    styleUrl: './tours.scss',
})
export class Tours implements AfterViewInit {
    private map!: leaflet.Map;
    private marker?: leaflet.Marker;
    selectingLocation = false;
    selectedLocation: Subject<{ latitude: number, longitude: number }> = new Subject();
    tourData: TourModel[] = [];

    constructor(private tourService: TourService) {
        this.tourService.fetchAllTours(tours => {
            console.log("TOUR DATA");
            console.log(tours);
            this.tourData = tours;
        });
    }

    ngAfterViewInit(): void {
        this.initMap();
    }

    private initMap(): void {
        this.map = leaflet.map('map').setView([48.2082, 16.3738], 13);

        leaflet.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; OpenStreetMap contributors'
        }).addTo(this.map);

        leaflet.marker([48.2082, 16.3738])
            .addTo(this.map)
            .bindPopup('Vienna')
            .openPopup();

        this.map.on('click', (e: leaflet.LeafletMouseEvent) => {
            if (!this.selectingLocation) return;

            const {lat, lng} = e.latlng;
            this.selectedLocation.next({latitude: lat, longitude: lng});

            if (this.marker) {
                this.map.removeLayer(this.marker);
            }

            this.marker = leaflet.marker([lat, lng]).addTo(this.map);

            console.log('Selected location:', this.selectedLocation);

            this.selectingLocation = false;
            this.map.getContainer().style.cursor = '';
        });
    }

    startLocationSelection() {
        this.selectingLocation = true;
        this.map.getContainer().style.cursor = 'crosshair';
    }
}
