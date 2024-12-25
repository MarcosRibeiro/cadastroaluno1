import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InformacoesComplementaresComponent } from './informacoes-complementares.component';

describe('InformacoesComplementaresComponent', () => {
  let component: InformacoesComplementaresComponent;
  let fixture: ComponentFixture<InformacoesComplementaresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InformacoesComplementaresComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(InformacoesComplementaresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
