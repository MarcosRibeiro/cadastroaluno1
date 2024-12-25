import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContatoEmergenciaComponent } from './contato-emergencia.component';

describe('ContatoEmergenciaComponent', () => {
  let component: ContatoEmergenciaComponent;
  let fixture: ComponentFixture<ContatoEmergenciaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContatoEmergenciaComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ContatoEmergenciaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
