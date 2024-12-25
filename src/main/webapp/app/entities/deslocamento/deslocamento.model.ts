import { ICadastroAluno } from 'app/entities/cadastro-aluno/cadastro-aluno.model';

export interface IDeslocamento {
  id: number;
  nome?: string | null;
  grau?: string | null;
  cadastroAluno?: ICadastroAluno | null;
}

export type NewDeslocamento = Omit<IDeslocamento, 'id'> & { id: null };
