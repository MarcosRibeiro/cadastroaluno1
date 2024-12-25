import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeslocamentoComponent } from './deslocamento.component';

describe('DeslocamentoComponent', () => {
  let component: DeslocamentoComponent;
  let fixture: ComponentFixture<DeslocamentoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeslocamentoComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DeslocamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
