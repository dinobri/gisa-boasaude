import dayjs from 'dayjs/esm';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { IPlano } from 'app/entities/plano/plano.model';
import { EstadoCivil } from 'app/entities/enumerations/estado-civil.model';
import { Sexo } from 'app/entities/enumerations/sexo.model';
import { UF } from 'app/entities/enumerations/uf.model';
import { SituacaoAssociado } from 'app/entities/enumerations/situacao-associado.model';

export interface IAssociado {
  id?: number;
  nome?: string | null;
  sobrenome?: string | null;
  dataNascimento?: dayjs.Dayjs | null;
  estadoCivil?: EstadoCivil | null;
  sexo?: Sexo | null;
  naturalidadeUf?: UF | null;
  naturalidadeCidade?: string | null;
  numeroDocumento?: string | null;
  ufDocumento?: UF | null;
  orgaoDocumento?: string | null;
  dataDocumento?: dayjs.Dayjs | null;
  nomeMae?: string | null;
  nomePai?: string | null;
  email?: string | null;
  telefone?: string | null;
  sitaucao?: SituacaoAssociado | null;
  endereco?: IEndereco | null;
  plano?: IPlano | null;
}

export class Associado implements IAssociado {
  constructor(
    public id?: number,
    public nome?: string | null,
    public sobrenome?: string | null,
    public dataNascimento?: dayjs.Dayjs | null,
    public estadoCivil?: EstadoCivil | null,
    public sexo?: Sexo | null,
    public naturalidadeUf?: UF | null,
    public naturalidadeCidade?: string | null,
    public numeroDocumento?: string | null,
    public ufDocumento?: UF | null,
    public orgaoDocumento?: string | null,
    public dataDocumento?: dayjs.Dayjs | null,
    public nomeMae?: string | null,
    public nomePai?: string | null,
    public email?: string | null,
    public telefone?: string | null,
    public sitaucao?: SituacaoAssociado | null,
    public endereco?: IEndereco | null,
    public plano?: IPlano | null
  ) {}
}

export function getAssociadoIdentifier(associado: IAssociado): number | undefined {
  return associado.id;
}
