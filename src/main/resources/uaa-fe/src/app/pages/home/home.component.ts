import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {ShapeComponent} from '../public/componants/shape/shape.component';

@Component({
  selector: 'app-home',
  imports: [
    RouterOutlet,
    ShapeComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  title: string = 'Make cool store easily with Cvhome';
  desc: string = 'Join our clients and try create your store with few clicks for free.';
}
