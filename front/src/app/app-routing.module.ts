import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

//Components
import { AccueilComponent } from './pages/accueil/accueil.component';
import { WikiItemsComponent } from './pages/wiki-items/wiki-items.component';

const routes: Routes = [
  { path: 'accueil', component: AccueilComponent },
  { path: 'wiki-items', component: WikiItemsComponent },
  { path: '', redirectTo: '/accueil', pathMatch: 'full' },
  { path: '**', redirectTo: '/accueil' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
