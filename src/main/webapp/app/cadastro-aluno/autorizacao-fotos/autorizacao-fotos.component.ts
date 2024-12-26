import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-autorizacao-fotos',
  templateUrl: './autorizacao-fotos.component.html',
  styleUrls: ['./autorizacao-fotos.component.css'],
})
export class AutorizacaoFotosComponent {
  @Input() form!: FormGroup; // Removido o <any>
  fotoAlunoPreview: string | ArrayBuffer | null = null;
  fotoMaePreview: string | ArrayBuffer | null = null;

  onFotoAlunoChange(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.form.patchValue({ fotoAluno: file });
      const reader = new FileReader();
      reader.onload = () => {
        this.fotoAlunoPreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onFotoMaeChange(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.form.patchValue({ fotoMae: file });
      const reader = new FileReader();
      reader.onload = () => {
        this.fotoMaePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }
}
