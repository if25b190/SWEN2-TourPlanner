import {AfterViewInit, Component, model} from '@angular/core';
import {TourService} from "../../service/tour";
import {FormsModule} from "@angular/forms";
import * as leaflet from "leaflet";
import {TourModal} from "../tour-modal/tour-modal";
import {Subject} from "rxjs";
import {TourModel} from "../../model/m_tour";
import {TourEditModal} from "../tour-edit-modal/tour-edit-modal";
import {TourDeleteModal} from "../tour-delete-modal/tour-delete-modal";
import {TourLogs} from "../tour-logs/tour-logs";
import {TourLogsAddModal} from "../tour-logs-add-modal/tour-logs-add-modal";
import {ToastrService} from "ngx-toastr";

@Component({
    selector: 'app-tours',
    imports: [
        FormsModule,
        TourModal,
        TourEditModal,
        TourDeleteModal,
        TourLogs,
        TourLogsAddModal
    ],
    templateUrl: './tours.html',
    styleUrl: './tours.scss',
})
export class Tours implements AfterViewInit {
    private map!: leaflet.Map;
    private marker?: leaflet.Marker;
    private tourMarkers: leaflet.Marker[] = [];
    private tourRouteLine?: leaflet.Polyline;
    selectedTour = model<TourModel>();
    selectingLocation = false;
    selectedLocation: Subject<{ latitude: number, longitude: number }> = new Subject();
    tourData: TourModel[] = [];
    private readonly uploadingTourUuids = new Set<string>();
    private readonly tourImageVersions: Record<string, number> = {};

    constructor(private tourService: TourService, private toastr: ToastrService) {
    }

    ngAfterViewInit(): void {
        this.fetchAllTours();
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

    searchTours(searchTerm: string): void {
        if (searchTerm.length < 1) {
            this.fetchAllTours();
            return;
        }
        this.tourService.searchTours(searchTerm, tours => {
            console.log("TOUR DATA");
            console.log(tours);
            this.tourData = tours;
        });
    }

    fetchAllTours(): void {
        this.tourService.fetchAllTours(tours => {
            console.log("TOUR DATA");
            console.log(tours);
            this.tourData = tours;
        });
    }

    getTourImageUrl(tour: TourModel): string {
        if (!tour.uuid) {
            return this.getTourImageFallbackUrl(tour);
        }

        return this.tourService.getTourFileUrl(tour.uuid, this.tourImageVersions[tour.uuid] ?? 0);
    }

    getTourImageFallbackUrl(tour: TourModel): string {
        return `https://picsum.photos/seed/${encodeURIComponent(tour.uuid ?? tour.name ?? 'tour')}/536/354`;
    }

    onTourImageError(event: Event, tour: TourModel): void {
        const image = event.target as HTMLImageElement;
        image.onerror = null;
        image.src = this.getTourImageFallbackUrl(tour);
    }

    onTourFileSelected(tour: TourModel, event: Event): void {
        event.stopPropagation();

        const input = event.target as HTMLInputElement;
        const file = input.files?.item(0);
        if (!file) {
            return;
        }

        if (file.type && file.type !== "image/png") {
            this.toastr.error("Upload a PNG image!");
            input.value = "";
            return;
        }

        if (!tour.uuid) {
            this.toastr.error("Tour must be saved before uploading an image!");
            input.value = "";
            return;
        }

        if (this.uploadingTourUuids.has(tour.uuid)) {
            input.value = "";
            return;
        }

        this.uploadingTourUuids.add(tour.uuid);
        const clearUploadState = () => {
            this.uploadingTourUuids.delete(tour.uuid!);
            input.value = "";
        };

        this.tourService.uploadTourFile(tour.uuid, file, () => {
            this.toastr.success("Tour image uploaded!");
            this.tourImageVersions[tour.uuid!] = Date.now();
            clearUploadState();
        }, clearUploadState);
    }

    isTourFileUploading(tour: TourModel): boolean {
        return !!tour.uuid && this.uploadingTourUuids.has(tour.uuid);
    }

    startLocationSelection() {
        this.selectingLocation = true;
        this.map.getContainer().style.cursor = 'crosshair';
    }

    selectTour(tour: TourModel) {
        if (this.selectedTour()?.uuid === tour.uuid) {
            this.selectedTour.set(undefined);
            this.clearTourMarkers();
        } else {
            this.selectedTour.set(undefined);
            this.clearTourMarkers();
            this.displayTourLocations(tour);
            setTimeout(() => this.selectedTour.set(tour), 200);
        }
    }

    private displayTourLocations(tour: TourModel): void {
        const {from, to} = tour;
        if (!from && !to) return;

        const fromIcon = leaflet.divIcon({
            className: '',
            html: `<div style="background:#22c55e;width:14px;height:14px;border-radius:50%;border:2px solid white;box-shadow:0 1px 4px rgba(0,0,0,0.4)"></div>`,
            iconSize: [14, 14],
            iconAnchor: [7, 7],
        });
        const toIcon = leaflet.divIcon({
            className: '',
            html: `<div style="background:#ef4444;width:14px;height:14px;border-radius:50%;border:2px solid white;box-shadow:0 1px 4px rgba(0,0,0,0.4)"></div>`,
            iconSize: [14, 14],
            iconAnchor: [7, 7],
        });

        const points: leaflet.LatLngTuple[] = [];

        if (from) {
            const m = leaflet.marker([from.latitude, from.longitude], {icon: fromIcon})
                .addTo(this.map)
                .bindPopup(`<b>Start</b><br>${from.latitude.toFixed(5)}, ${from.longitude.toFixed(5)}`);
            this.tourMarkers.push(m);
            points.push([from.latitude, from.longitude]);
        }

        if (to) {
            const m = leaflet.marker([to.latitude, to.longitude], {icon: toIcon})
                .addTo(this.map)
                .bindPopup(`<b>Destination</b><br>${to.latitude.toFixed(5)}, ${to.longitude.toFixed(5)}`);
            this.tourMarkers.push(m);
            points.push([to.latitude, to.longitude]);
        }

        if (points.length === 2) {
            this.tourRouteLine = leaflet.polyline(points, {color: '#6366f1', weight: 3, dashArray: '6 6'})
                .addTo(this.map);
        }

        this.map.fitBounds(leaflet.latLngBounds(points), {padding: [48, 48]});
    }

    private clearTourMarkers(): void {
        this.tourMarkers.forEach(m => this.map.removeLayer(m));
        this.tourMarkers = [];
        if (this.tourRouteLine) {
            this.map.removeLayer(this.tourRouteLine);
            this.tourRouteLine = undefined;
        }
    }
}
