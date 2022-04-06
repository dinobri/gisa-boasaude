import { IEndereco } from 'app/entities/endereco/endereco.model';
import { ITipoExame } from 'app/entities/tipo-exame/tipo-exame.model';

export interface IConveniado {
  id?: number;
  nome?: string | null;
  telefone?: string | null;
  endereco?: IEndereco | null;
  tipoExames?: ITipoExame[] | null;
}

export class Conveniado implements IConveniado {
  constructor(
    public id?: number,
    public nome?: string | null,
    public telefone?: string | null,
    public endereco?: IEndereco | null,
    public tipoExames?: ITipoExame[] | null
  ) {}
}

export function getConveniadoIdentifier(conveniado: IConveniado): number | undefined {
  return conveniado.id;
}
