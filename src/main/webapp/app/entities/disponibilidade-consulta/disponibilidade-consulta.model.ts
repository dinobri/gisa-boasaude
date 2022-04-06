import dayjs from 'dayjs/esm';
import { IPrestador } from 'app/entities/prestador/prestador.model';

export interface IDisponibilidadeConsulta {
  id?: number;
  horaInicio?: dayjs.Dayjs | null;
  horaFim?: dayjs.Dayjs | null;
  prestador?: IPrestador | null;
}

export class DisponibilidadeConsulta implements IDisponibilidadeConsulta {
  constructor(
    public id?: number,
    public horaInicio?: dayjs.Dayjs | null,
    public horaFim?: dayjs.Dayjs | null,
    public prestador?: IPrestador | null
  ) {}
}

export function getDisponibilidadeConsultaIdentifier(disponibilidadeConsulta: IDisponibilidadeConsulta): number | undefined {
  return disponibilidadeConsulta.id;
}
