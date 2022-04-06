import { UF } from 'app/entities/enumerations/uf.model';

export interface IEndereco {
  id?: number;
  cep?: string | null;
  logradouro?: string | null;
  numero?: string | null;
  cidade?: string | null;
  uf?: UF | null;
  bairro?: string | null;
  complemento?: string | null;
}

export class Endereco implements IEndereco {
  constructor(
    public id?: number,
    public cep?: string | null,
    public logradouro?: string | null,
    public numero?: string | null,
    public cidade?: string | null,
    public uf?: UF | null,
    public bairro?: string | null,
    public complemento?: string | null
  ) {}
}

export function getEnderecoIdentifier(endereco: IEndereco): number | undefined {
  return endereco.id;
}
