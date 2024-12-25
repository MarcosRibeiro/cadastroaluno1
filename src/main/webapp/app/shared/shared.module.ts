import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { CadastroAlunoModule } from '../cadastro-aluno/cadastro-aluno.module'; // Importe o seu módulo aqui

import FindLanguageFromKeyPipe from './language/find-language-from-key.pipe';
import TranslateDirective from './language/translate.directive';
import AlertComponent from './alert/alert.component';
import AlertErrorComponent from './alert/alert-error.component';
import HasAnyAuthorityDirective from './auth/has-any-authority.directive';
import DurationPipe from './date/duration.pipe';
import FormatMediumDatetimePipe from './date/format-medium-datetime.pipe';
import FormatMediumDatePipe from './date/format-medium-date.pipe';
import SortByDirective from './sort/sort-by.directive';
import SortDirective from './sort/sort.directive';
import ItemCountComponent from './pagination/item-count.component';

@NgModule({
  imports: [
    CommonModule,
    NgbModule,
    FontAwesomeModule,
    TranslateModule,
    CadastroAlunoModule, // Adicione o módulo na lista de imports
  ],
  declarations: [
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
  ],
  exports: [
    CommonModule,
    NgbModule,
    FontAwesomeModule,
    TranslateModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
  ],
})
export class SharedModule {}
