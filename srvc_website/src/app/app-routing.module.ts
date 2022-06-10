import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IgxListModule} from "igniteui-angular";

const routes: Routes = [];

@NgModule({
  imports: [RouterModule.forRoot(routes),IgxListModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
