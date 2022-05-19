import { Component, OnInit } from '@angular/core';
import { IPlace } from '../place.model';
import { PlaceService } from '../service/place.service';

@Component({
  selector: 'jhi-places-map',
  templateUrl: './places-map.component.html',
  styleUrls: ['./places-map.component.scss'],
})
export class PlacesMapComponent implements OnInit {
  latitude?: number;
  longitude?: number;
  zoom?: number;

  places?: IPlace[];

  constructor(protected placeService: PlaceService) {}

  ngOnInit(): void {
    this.setCurrentLocation();
    this.placeService.query().subscribe(res => {
      if (res.body) {
        this.places = res.body;
      }
    });
  }

  getIconUrl(place: IPlace): string {
    if (place.type === 'Help') {
      return 'https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|428604';
    } else if (place.type === 'Shelter') {
      return 'https://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|4286f4';
    }
    return '';
  }

  private setCurrentLocation(): void {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition(position => {
        this.latitude = position.coords.latitude;
        this.longitude = position.coords.longitude;
        this.zoom = 10;
      });
    }
  }
}
