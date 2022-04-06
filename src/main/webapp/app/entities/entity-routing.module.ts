import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'associado',
        data: { pageTitle: 'Associados' },
        loadChildren: () => import('./associado/associado.module').then(m => m.AssociadoModule),
      },
      {
        path: 'prestador',
        data: { pageTitle: 'Prestadors' },
        loadChildren: () => import('./prestador/prestador.module').then(m => m.PrestadorModule),
      },
      {
        path: 'conveniado',
        data: { pageTitle: 'Conveniados' },
        loadChildren: () => import('./conveniado/conveniado.module').then(m => m.ConveniadoModule),
      },
      {
        path: 'plano',
        data: { pageTitle: 'Planos' },
        loadChildren: () => import('./plano/plano.module').then(m => m.PlanoModule),
      },
      {
        path: 'consulta',
        data: { pageTitle: 'Consultas' },
        loadChildren: () => import('./consulta/consulta.module').then(m => m.ConsultaModule),
      },
      {
        path: 'disponibilidade-consulta',
        data: { pageTitle: 'DisponibilidadeConsultas' },
        loadChildren: () => import('./disponibilidade-consulta/disponibilidade-consulta.module').then(m => m.DisponibilidadeConsultaModule),
      },
      {
        path: 'exame',
        data: { pageTitle: 'Exames' },
        loadChildren: () => import('./exame/exame.module').then(m => m.ExameModule),
      },
      {
        path: 'disponibilidade-exame',
        data: { pageTitle: 'DisponibilidadeExames' },
        loadChildren: () => import('./disponibilidade-exame/disponibilidade-exame.module').then(m => m.DisponibilidadeExameModule),
      },
      {
        path: 'tipo-exame',
        data: { pageTitle: 'TipoExames' },
        loadChildren: () => import('./tipo-exame/tipo-exame.module').then(m => m.TipoExameModule),
      },
      {
        path: 'endereco',
        data: { pageTitle: 'Enderecos' },
        loadChildren: () => import('./endereco/endereco.module').then(m => m.EnderecoModule),
      },
      {
        path: 'especialidade',
        data: { pageTitle: 'Especialidades' },
        loadChildren: () => import('./especialidade/especialidade.module').then(m => m.EspecialidadeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
