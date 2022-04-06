import dayjs from 'dayjs/esm';
import { IPrestador } from 'app/entities/prestador/prestador.model';
import { ITipoExame } from 'app/entities/tipo-exame/tipo-exame.model';

export interface IDisponibilidadeExame {
  id?: number;
  horaInicio?: dayjs.Dayjs | null;
  horaFim?: dayjs.Dayjs | null;
  prestador?: IPrestador | null;
  tipoExame?: ITipoExame | null;
}

export class DisponibilidadeExame implements IDisponibilidadeExame {
  constructor(
    public id?: number,
    public horaInicio?: dayjs.Dayjs | null,
    public horaFim?: dayjs.Dayjs | null,
    public prestador?: IPrestador | null,
    public tipoExame?: ITipoExame | null
  ) {}
}

export function getDisponibilidadeExameIdentifier(disponibilidadeExame: IDisponibilidadeExame): number | undefined {
  return disponibilidadeExame.id;
}
