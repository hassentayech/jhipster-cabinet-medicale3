import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'patient',
        data: { pageTitle: 'Patients' },
        loadChildren: () => import('./patient/patient.module').then(m => m.PatientModule),
      },
      {
        path: 'rendez-vous',
        data: { pageTitle: 'RendezVous' },
        loadChildren: () => import('./rendez-vous/rendez-vous.module').then(m => m.RendezVousModule),
      },
      {
        path: 'antecedent',
        data: { pageTitle: 'Antecedents' },
        loadChildren: () => import('./antecedent/antecedent.module').then(m => m.AntecedentModule),
      },
      {
        path: 'cas-traiter',
        data: { pageTitle: 'CasTraiters' },
        loadChildren: () => import('./cas-traiter/cas-traiter.module').then(m => m.CasTraiterModule),
      },
      {
        path: 'visite',
        data: { pageTitle: 'Visites' },
        loadChildren: () => import('./visite/visite.module').then(m => m.VisiteModule),
      },
      {
        path: 'constante',
        data: { pageTitle: 'Constantes' },
        loadChildren: () => import('./constante/constante.module').then(m => m.ConstanteModule),
      },
      {
        path: 'ordonnance',
        data: { pageTitle: 'Ordonnances' },
        loadChildren: () => import('./ordonnance/ordonnance.module').then(m => m.OrdonnanceModule),
      },
      {
        path: 'reglement',
        data: { pageTitle: 'Reglements' },
        loadChildren: () => import('./reglement/reglement.module').then(m => m.ReglementModule),
      },
      {
        path: 'certificat',
        data: { pageTitle: 'Certificats' },
        loadChildren: () => import('./certificat/certificat.module').then(m => m.CertificatModule),
      },
      {
        path: 'cr-echographie',
        data: { pageTitle: 'CrEchographies' },
        loadChildren: () => import('./cr-echographie/cr-echographie.module').then(m => m.CrEchographieModule),
      },
      {
        path: 'prescription',
        data: { pageTitle: 'Prescriptions' },
        loadChildren: () => import('./prescription/prescription.module').then(m => m.PrescriptionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
