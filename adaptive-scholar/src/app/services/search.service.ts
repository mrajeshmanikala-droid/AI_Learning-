import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private querySignal = signal<string>('');
  
  // Expose as read-only
  query = this.querySignal.asReadonly();

  setQuery(val: string) {
    this.querySignal.set(val);
  }

  clearQuery() {
    this.querySignal.set('');
  }
}
