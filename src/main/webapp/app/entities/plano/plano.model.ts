import { CategoriaPlano } from 'app/entities/enumerations/categoria-plano.model';
import { TipoPlano } from 'app/entities/enumerations/tipo-plano.model';

export interface IPlano {
  id?: number;
  nome?: string | null;
  codigoANS?: string | null;
  categoria?: CategoriaPlano | null;
  tipo?: TipoPlano | null;
  odonto?: boolean | null;
  idadeMin?: number | null;
  idadeMax?: number | null;
  quantidadeConsultasAno?: number | null;
  quanatidadeExamesAno?: number | null;
  valorMensalidade?: number | null;
}

export class Plano implements IPlano {
  constructor(
    public id?: number,
    public nome?: string | null,
    public codigoANS?: string | null,
    public categoria?: CategoriaPlano | null,
    public tipo?: TipoPlano | null,
    public odonto?: boolean | null,
    public idadeMin?: number | null,
    public idadeMax?: number | null,
    public quantidadeConsultasAno?: number | null,
    public quanatidadeExamesAno?: number | null,
    public valorMensalidade?: number | null
  ) {
    this.odonto = this.odonto ?? false;
  }
}

export function getPlanoIdentifier(plano: IPlano): number | undefined {
  return plano.id;
}
