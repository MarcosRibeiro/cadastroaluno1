import { IResponsavel, NewResponsavel } from './responsavel.model';

export const sampleWithRequiredData: IResponsavel = {
  id: 10442,
  nome: 'huzzah onto',
  parentesco: 'baggy sand yet',
};

export const sampleWithPartialData: IResponsavel = {
  id: 14907,
  nome: 'pension lock',
  parentesco: 'violently',
};

export const sampleWithFullData: IResponsavel = {
  id: 27883,
  nome: 'flimsy while',
  parentesco: 'motivate',
};

export const sampleWithNewData: NewResponsavel = {
  nome: 'gestate metabolite quarterly',
  parentesco: 'guzzle badly next',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
