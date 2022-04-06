import { IConveniado } from 'app/entities/conveniado/conveniado.model';

export interface ITipoExame {
  id?: number;
  nome?: string | null;
  conveniados?: IConveniado[] | null;
}

export class TipoExame implements ITipoExame {
  constructor(public id?: number, public nome?: string | null, public conveniados?: IConveniado[] | null) {}
}

export function getTipoExameIdentifier(tipoExame: ITipoExame): number | undefined {
  return tipoExame.id;
}
