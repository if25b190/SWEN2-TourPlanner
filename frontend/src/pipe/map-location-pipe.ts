import { Pipe, PipeTransform } from '@angular/core';
import {MapLocation} from "../model/m_tour";

@Pipe({
  name: 'mapLocation',
})
export class MapLocationPipe implements PipeTransform {

  transform(value: MapLocation | undefined): string {
    return `Lat: ${value?.latitude.toFixed(4) ?? "?"}, Long: ${value?.longitude.toFixed(4) ?? "?"}`;
  }

}
