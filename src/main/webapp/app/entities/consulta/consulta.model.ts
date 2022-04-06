import { IDisponibilidadeConsulta } from 'app/entities/disponibilidade-consulta/disponibilidade-consulta.model';
import { IAssociado } from 'app/entities/associado/associado.model';
import { SituacaoAtendimento } from 'app/entities/enumerations/situacao-atendimento.model';

export interface IConsulta {
  id?: number;
  situacao?: SituacaoAtendimento | null;
  disponibilidadeConsulta?: IDisponibilidadeConsulta | null;
  associado?: IAssociado | null;
}

export class Consulta implements IConsulta {
  constructor(
    public id?: number,
    public situacao?: SituacaoAtendimento | null,
    public disponibilidadeConsulta?: IDisponibilidadeConsulta | null,
    public associado?: IAssociado | null
  ) {}
}

export function getConsultaIdentifier(consulta: IConsulta): number | undefined {
  return consulta.id;
}
