export interface IEspecialidade {
  id?: number;
  nome?: string | null;
}

export class Especialidade implements IEspecialidade {
  constructor(public id?: number, public nome?: string | null) {}
}

export function getEspecialidadeIdentifier(especialidade: IEspecialidade): number | undefined {
  return especialidade.id;
}
