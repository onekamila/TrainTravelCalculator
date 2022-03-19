import { Component, Inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Validators, FormGroup, FormBuilder, NgForm, FormControl, FormGroupDirective, AbstractControl, ValidatorFn } from '@angular/forms';

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
  delays: number[];

  public mockImagePath: string;
  public trainsDisplay: TrainInfo[];

  constructor(http: HttpClient, formBuilder: FormBuilder, @Inject('BASE_URL') baseUrl: string) {

    this.origin = new FormControl('');
    this.destination = new FormControl('');
    this.departure_date = new FormControl('');

    this.trainQueryForm = formBuilder.group({
      'origin': this.origin,
      'destination': this.destination,
      'departure_date': this.departure_date
    });

    this.mockImagePath = '/assets/images/trainMapMock.png';
    this.rootUrl = baseUrl;
  }

  onFormSubmit(form: NgForm, http: HttpClient) {
    const headers = new HttpHeaders()
      .append(
        'Content-Type',
        'application/json'
    );

    // Get historical departure delay and arrival delay
    http.post<number[]>(this.rootUrl + 'traintrack', JSON.stringify(form), {
      headers: headers
    }).subscribe(result => {
      this.delays = result;
    }, error => console.error(error));

    // Get scraping info for trains
    http.get<TrainInfo[]>('http://127.0.0.1:8080/?origin=' + this.origin + '&destination=' + this.destination + '&departure_date=' + this.departure_date).subscribe(result => {
      this.trainsDisplay = result;
    }, error => console.error(error));
  }
}

interface TrainInfo {
  origin: string;
  dest: string;
  train_name: string;
  scheduled_departure: string;
  scheduled_arrival: string;
}
