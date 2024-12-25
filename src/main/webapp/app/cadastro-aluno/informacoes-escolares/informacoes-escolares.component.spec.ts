import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InformacoesEscolaresComponent } from './informacoes-escolares.component';

describe('InformacoesEscolaresComponent', () => {
  let component: InformacoesEscolaresComponent;
  let fixture: ComponentFixture<InformacoesEscolaresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InformacoesEscolaresComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(InformacoesEscolaresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
