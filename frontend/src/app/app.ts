import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { User } from './core/models/user.model';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class AppComponent {

  httpClient:HttpClient = inject(HttpClient);

  title = 'sample-app-frontend';

  usersLoaded = signal(false);

  users:User[]= [];

  callBackend() {
    this.usersLoaded.set(false);
    this.httpClient.get('/user').subscribe((response:any) => {
        this.users = response;
        this.usersLoaded.set(true);
    });
  }
}
