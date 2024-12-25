import { IDeslocamento, NewDeslocamento } from './deslocamento.model';

export const sampleWithRequiredData: IDeslocamento = {
  id: 2320,
  nome: 'condense urban unfortunately',
  grau: 'closed wetly',
};

export const sampleWithPartialData: IDeslocamento = {
  id: 25200,
  nome: 'mysterious affiliate stunt',
  grau: 'commandeer',
};

export const sampleWithFullData: IDeslocamento = {
  id: 3673,
  nome: 'suddenly apropos bah',
  grau: 'instead er',
};

export const sampleWithNewData: NewDeslocamento = {
  nome: 'deadly',
  grau: 'leading',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
