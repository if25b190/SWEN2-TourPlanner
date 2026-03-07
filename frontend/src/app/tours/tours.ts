import {AfterViewInit, Component} from '@angular/core';
import {TourService} from "../../service/tour";
import {FormsModule} from "@angular/forms";
import * as leaflet from "leaflet";
import {TourModal} from "../tour-modal/tour-modal";

@Component({
  selector: 'app-tours',
  imports: [
    FormsModule,
    TourModal
  ],
  templateUrl: './tours.html',
  styleUrl: './tours.scss',
})
export class Tours implements AfterViewInit {
  private map!: leaflet.Map;
  private marker?: leaflet.Marker;
  selectingLocation = false;
  selectedLocation?: {latitude: number, longitude: number};

  constructor(private tourService: TourService) {
    this.tourService.fetchAllTours(tours => {
      console.log("TOUR DATA");
      console.log(tours);
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

    this.map.on('click', (e: L.LeafletMouseEvent) => {
      if (!this.selectingLocation) return;

      const { lat, lng } = e.latlng;
      this.selectedLocation = { latitude: lat, longitude: lng };

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
