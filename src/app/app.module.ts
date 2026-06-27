import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgxKonvaModule } from 'ngx-konva';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CircuitSimulatorComponent } from './circuit-simulator/circuit-simulator.component';
import { CanvasComponent } from './canvas/canvas.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    AppComponent,
    CircuitSimulatorComponent,
    CanvasComponent
  ],
  imports: [
    BrowserModule,
    NgxKonvaModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule

  ],

  providers: [],
  bootstrap: [AppComponent],

})
export class AppModule { }
