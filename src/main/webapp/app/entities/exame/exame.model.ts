import { IDisponibilidadeExame } from 'app/entities/disponibilidade-exame/disponibilidade-exame.model';
import { IAssociado } from 'app/entities/associado/associado.model';
import { SituacaoAtendimento } from 'app/entities/enumerations/situacao-atendimento.model';

export interface IExame {
  id?: number;
  situacao?: SituacaoAtendimento | null;
  disponibilidadeExame?: IDisponibilidadeExame | null;
  associado?: IAssociado | null;
}

export class Exame implements IExame {
  constructor(
    public id?: number,
    public situacao?: SituacaoAtendimento | null,
    public disponibilidadeExame?: IDisponibilidadeExame | null,
    public associado?: IAssociado | null
  ) {}
}

export function getExameIdentifier(exame: IExame): number | undefined {
  return exame.id;
}
