import dayjs from 'dayjs/esm';

import { ICadastroAluno, NewCadastroAluno } from './cadastro-aluno.model';

export const sampleWithRequiredData: ICadastroAluno = {
  id: 28709,
  dataCadastro: dayjs('2024-12-25'),
  nome: 'above',
  dn: dayjs('2024-12-25'),
  cep: 'stylish ch',
  endereco: 'overconfidently out',
  fone: 'provided warped',
  cpf: 'eventually',
  filiacaoMae: 'flashy psst',
  contatoEmergencia: 'recommendation',
  foneEmergencia: 'judicious when ',
  relacaoEmergencia: 'finally',
  autorizacao: false,
};

export const sampleWithPartialData: ICadastroAluno = {
  id: 16927,
  dataCadastro: dayjs('2024-12-25'),
  matricula: 'lanky of beep',
  grupo: 'to stool',
  nome: 'microblog',
  dn: dayjs('2024-12-25'),
  cep: 'kiddingly ',
  endereco: 'beard hubris but',
  qd: 'blah',
  uf: 'dr',
  fone: 'bank hungry',
  termo: 'pfft following',
  cartorio: 'cinder microchip from',
  cpf: 'monthly even',
  nis: 'among when towa',
  filiacaoPai: 'gee gown evil',
  paiNaturalidade: 'mostly',
  paiDataNascimento: dayjs('2024-12-25'),
  paiCpf: 'bob woot know',
  paiMunicipio: 'sheepishly quick-witted freight',
  filiacaoMae: 'incidentally',
  maeTelefone: 'guest yak',
  maeRg: 'genuine',
  maeTituloEleitor: 'other',
  maeMunicipio: 'provided oh',
  nomeEscola: 'phooey',
  mediaEscolar: 24148.75,
  comportamentoEscola: 'AGITADO',
  medicacaoDesc: 'new',
  alergia: 'NAO',
  historicoMedico: 'pulse rout',
  pessoasTrabalham: 24548,
  beneficios: 'mixture enthusiastically excluding',
  situacaoResidencia: 'CEDIDA',
  situacaoResidenciaDesc: 'per',
  contatoEmergencia: 'than',
  foneEmergencia: 'qua',
  relacaoEmergencia: 'gosh sock',
  autorizacao: true,
  fotoMae: 'than punctually roughly',
};

export const sampleWithFullData: ICadastroAluno = {
  id: 8626,
  dataCadastro: dayjs('2024-12-25'),
  matricula: 'idealistic role viol',
  grupo: 'understanding',
  nome: 'like wavy sadly',
  dn: dayjs('2024-12-25'),
  cep: 'lovingly t',
  endereco: 'version',
  qd: 'safely',
  lote: 'gosh ah si',
  endnumero: 'feline',
  bairro: 'oh',
  municipio: 'violently hmph',
  uf: 'gn',
  fone: 'verbally',
  certidao: 'simplistic governance',
  termo: 'unto',
  cartorio: 'release gee',
  naturalidade: 'density since',
  rg: 'provider after',
  cpf: 'libel ouch tim',
  nis: 'achieve gripper',
  cras: 'discourse rapidly immediately',
  filiacaoPai: 'mmm',
  paiTelefone: 'spook',
  paiNaturalidade: 'cannibalise immediately what',
  paiUf: 'fi',
  paiRg: 'crank yum behind',
  paiDataNascimento: dayjs('2024-12-25'),
  paiCpf: 'satirise',
  paiNis: 'every bid now',
  paiTituloEleitor: 'among that deed',
  paiZona: 'unruly alo',
  paiSecao: 'hmph',
  paiMunicipio: 'although',
  filiacaoMae: 'necessary neglected enormously',
  maeTelefone: 'volleyball',
  maeNaturalidade: 'down who',
  maeUf: 'se',
  maeRg: 'yowza provided iride',
  maeDataNascimento: dayjs('2024-12-25'),
  maeCpf: 'biodegradable ',
  maeNis: 'redact',
  maeTituloEleitor: 'almost impostor',
  maeZona: 'broadly if',
  maeSecao: 'abaft larg',
  maeMunicipio: 'over',
  nomeEscola: 'though modulo briefly',
  anoCursando: 'oh softly integer',
  turno: 'MATUTINO',
  mediaEscolar: 7944.25,
  prioritario: 'SIM',
  obs: 'successfully rotten oily',
  comportamentoCasa: 'TRANQUILO',
  comportamentoEscola: 'AGITADO',
  deficiencia: 'SIM',
  adaptacoes: 'SIM',
  medicacao: 'NAO',
  medicacaoDesc: 'ouch mmm qua',
  alergia: 'SIM',
  alergiaDesc: 'aboard',
  historicoMedico: 'dock carelessly',
  rendaFamiliar: 'accidentally',
  pessoasTrabalham: 26838,
  numPessoasLar: 3118,
  beneficioSocial: 'SIM',
  beneficios: 'until only',
  tipoResidencia: 'OUTRO',
  tipoResidenciaDesc: 'until headline even',
  situacaoResidencia: 'PROPRIA',
  situacaoResidenciaDesc: 'coaxingly ill',
  contatoEmergencia: 'ick',
  foneEmergencia: 'why ambitious h',
  relacaoEmergencia: 'however',
  autorizacao: false,
  fotoAluno: 'intrepid although meh',
  fotoMae: 'quickly',
};

export const sampleWithNewData: NewCadastroAluno = {
  dataCadastro: dayjs('2024-12-25'),
  nome: 'lava closely',
  dn: dayjs('2024-12-25'),
  cep: 'worthless ',
  endereco: 'supposing',
  fone: 'cook for',
  cpf: 'lotion describ',
  filiacaoMae: 'wisely',
  contatoEmergencia: 'oil whenever upwardly',
  foneEmergencia: 'ick',
  relacaoEmergencia: 'terrorise unlike',
  autorizacao: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
