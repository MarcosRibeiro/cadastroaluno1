import { ICadastroAluno } from 'app/entities/cadastro-aluno/cadastro-aluno.model';

export interface IResponsavel {
  id: number;
  nome?: string | null;
  parentesco?: string | null;
  cadastroAluno?: ICadastroAluno | null;
}

export type NewResponsavel = Omit<IResponsavel, 'id'> & { id: null };
