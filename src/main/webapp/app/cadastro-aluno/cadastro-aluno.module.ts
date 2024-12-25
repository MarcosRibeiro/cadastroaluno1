import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';

import { DadosAlunoComponent } from './dados-aluno/dados-aluno.component';
import { EnderecoComponent } from './endereco/endereco.component';
import { DocumentosComponent } from './documentos/documentos.component';
import { FiliacaoComponent } from './filiacao/filiacao.component';
import { InformacoesEscolaresComponent } from './informacoes-escolares/informacoes-escolares.component';
import { InformacoesComplementaresComponent } from './informacoes-complementares/informacoes-complementares.component';
import { ResponsaveisComponent } from './responsaveis/responsaveis.component';
import { DeslocamentoComponent } from './deslocamento/deslocamento.component';
import { ContatoEmergenciaComponent } from './contato-emergencia/contato-emergencia.component';
import { AutorizacaoFotosComponent } from './autorizacao-fotos/autorizacao-fotos.component';
import { CadastroAlunoComponent } from './cadastro-aluno/cadastro-aluno.component';
import { CepService } from './cep.service';

@NgModule({
  declarations: [
    DadosAlunoComponent,
    EnderecoComponent,
    DocumentosComponent,
    FiliacaoComponent,
    InformacoesEscolaresComponent,
    InformacoesComplementaresComponent,
    ResponsaveisComponent,
    DeslocamentoComponent,
    ContatoEmergenciaComponent,
    AutorizacaoFotosComponent,
    CadastroAlunoComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCheckboxModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    NgxMaskDirective,
    NgxMaskPipe,
  ],
  providers: [CepService, provideNgxMask()],
  exports: [CadastroAlunoComponent],
})
export class CadastroAlunoModule {}
