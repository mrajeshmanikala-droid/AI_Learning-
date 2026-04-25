import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserProgress {
  courseId: number;
  progress: number;
  completedLessons: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProgressService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/progress';

  private get headers() {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  /** Get all progress entries for the logged-in user */
  getUserProgress(): Observable<UserProgress[]> {
    return this.http.get<UserProgress[]>(this.API_URL, { headers: this.headers });
  }

  /** Update progress for a specific course */
  updateProgress(courseId: number, progress: number, completedLessons: number): Observable<UserProgress> {
    return this.http.put<UserProgress>(
      `${this.API_URL}/${courseId}`,
      { progress, completedLessons },
      { headers: this.headers }
    );
  }
}
