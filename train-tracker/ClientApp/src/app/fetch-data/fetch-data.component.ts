import { Component, Inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Validators, FormGroup, FormBuilder, NgForm, FormControl, FormGroupDirective, AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-fetch-data',
  templateUrl: './fetch-data.component.html'
})
export class FetchDataComponent {
  trainQueryForm: FormGroup;
  rootUrl: string;

  origin: FormControl;
  destination: FormControl;
  departure_date: FormControl;
  scrapedTrains: ScrapedTrainInfo[];

  public mockImagePath: string;
  public displayTrains: DisplayTrainInfo[];

  constructor(http: HttpClient, formBuilder: FormBuilder, @Inject('BASE_URL') baseUrl: string) {

    this.origin = new FormControl('');
    this.destination = new FormControl('');
    this.departure_date = new FormControl('');

    this.trainQueryForm = formBuilder.group({
      'origin_station_id': this.origin,
      'destination_station_id': this.destination,
      'train_id': '',
      'scheduled_departure': '',
      'scheduled_arrival': ''
    });

    this.mockImagePath = '/assets/images/trainMapMock.png';
    this.rootUrl = baseUrl;
  }

  onFormSubmit(form: NgForm, http: HttpClient) {
    const headers = new HttpHeaders()
      .append('Content-Type', 'application/json');

    // Get scraping info for trains
    http.get<ScrapedTrainInfo[]>('http://127.0.0.1:8080/?origin=' + this.origin + '&destination=' + this.destination + '&departure_date=' + this.departure_date).subscribe(result => {
      // Example returned data
      //[
      //   {
      //     "scheduled_arrival": "1:52p", 
      //     "scheduled_departure": "8:43a", 
      //     "train_name": "2154"
      //
      //   }
      // ]
      this.scrapedTrains = result;
    }, error => console.error(error));

    // Get historical departure delay and arrival delay for each train
    for (var i = 0; i < this.scrapedTrains.length; i++) {
      this.trainQueryForm.get("train_id").setValue(this.scrapedTrains[i].train_name);

      // Get historical departure delay and arrival delay for a train
      http.post<DisplayTrainInfo[]>(this.rootUrl + 'traintrack', JSON.stringify(this.trainQueryForm), {
        headers: headers
      }).subscribe(result => {
        this.displayTrains = result;
      }, error => console.error(error));
    }
  }
}

interface ScrapedTrainInfo {
  train_name: string;
  scheduled_departure: string;
  scheduled_arrival: string;
}

interface DisplayTrainInfo {
  train_name: string;
  route_name: string;
  scheduled_departure: string;
  scheduled_arrival: string;
  historical_departure_delay: string;
  historical_arrival_delay: string;
}
