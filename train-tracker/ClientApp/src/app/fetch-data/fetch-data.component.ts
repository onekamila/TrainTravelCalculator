import { Component, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Validators, FormGroup, FormBuilder, NgForm, FormControl, FormGroupDirective, AbstractControl, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-fetch-data',
  templateUrl: './fetch-data.component.html'
})
export class FetchDataComponent {
  trainQueryForm: FormGroup;

  origin: FormControl;
  dstination: FormControl;

  public mockImagePath: string;
  public trains: TrainInformation[];
  public trainsScrape: TrainScrapeInfo[];

  constructor(http: HttpClient, @Inject('BASE_URL') baseUrl: string) {
    this.mockImagePath = '/assets/images/trainMapMock.png';

    http.get<TrainInformation[]>(baseUrl + 'traintrack').subscribe(result => {
      this.trains = result;
    }, error => console.error(error));

    http.get<TrainScrapeInfo[]>('http://127.0.0.1:8080/?origin=Philly&destination=NewYork').subscribe(result => {
      this.trainsScrape = result;
      console.log(result);
    }, error => console.error(error));
  }
}

interface TrainInformation {
  lineName: string;
  historicalTimeDelay: string;
  currentStatus: string;
  currentDepartureTime: string;
  scheduledDepartureTime: string;
}

interface TrainScrapeInfo {
  origin: string;
  dest: string;
  train_name: string;
  scheduled_departure: string;
  scheduled_arrival: string;
}
