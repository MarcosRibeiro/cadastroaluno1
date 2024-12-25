import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AutorizacaoFotosComponent } from './autorizacao-fotos.component';

describe('AutorizacaoFotosComponent', () => {
  let component: AutorizacaoFotosComponent;
  let fixture: ComponentFixture<AutorizacaoFotosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AutorizacaoFotosComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AutorizacaoFotosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
