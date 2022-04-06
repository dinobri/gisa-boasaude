import dayjs from 'dayjs/esm';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { IEspecialidade } from 'app/entities/especialidade/especialidade.model';
import { Sexo } from 'app/entities/enumerations/sexo.model';

export interface IPrestador {
  id?: number;
  nome?: string | null;
  sobrenome?: string | null;
  dataNascimento?: dayjs.Dayjs | null;
  sexo?: Sexo | null;
  numeroRegistroProfissional?: string | null;
  telefone?: string | null;
  enderecoAtendimento?: IEndereco | null;
  especialidade?: IEspecialidade | null;
}

export class Prestador implements IPrestador {
  constructor(
    public id?: number,
    public nome?: string | null,
    public sobrenome?: string | null,
    public dataNascimento?: dayjs.Dayjs | null,
    public sexo?: Sexo | null,
    public numeroRegistroProfissional?: string | null,
    public telefone?: string | null,
    public enderecoAtendimento?: IEndereco | null,
    public especialidade?: IEspecialidade | null
  ) {}
}

export function getPrestadorIdentifier(prestador: IPrestador): number | undefined {
  return prestador.id;
}
