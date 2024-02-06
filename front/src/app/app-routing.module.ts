import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

//Components
import { AccueilComponent } from './pages/accueil/accueil.component';
import { WikiItemsComponent } from './pages/wiki-items/wiki-items.component';
import { BuildsComponent } from './pages/builds/builds.component';
import { CreateBuildComponent } from './pages/create-build/create-build.component';
import { LoginComponent } from './pages/login/login.component';
import { RgpdComponent } from './pages/rgpd/rgpd.component';

const routes: Routes = [
  { path: 'accueil', component: AccueilComponent },
  { path: 'wiki-items', component: WikiItemsComponent },
  { path: 'liste-builds', component: BuildsComponent},
  { path: 'create-build', component: CreateBuildComponent},
  { path: 'login', component: LoginComponent},
  { path: 'mentions', component: RgpdComponent},
  { path: 'rgpd', component: RgpdComponent},
  { path: '', redirectTo: '/accueil', pathMatch: 'full' },
  { path: '**', redirectTo: '/accueil' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
