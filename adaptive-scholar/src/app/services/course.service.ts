import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Lesson {
  id: number;
  title: string;
  description: string;
  videoUrl: string;
  duration: string;
  module: string;
}

export interface Course {
  id: number;
  title: string;
  description: string;
  image: string;
  progress: number;
  modulesCount: number;
  lessons: Lesson[];
}

export interface Question {
  id: number;
  text: string;
  options: string[];
  correctOptionIndex: number;
  explanation: string;
}

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/courses';

  private get headers() {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.API_URL, { headers: this.headers });
  }

  getCourseById(id: number): Observable<Course> {
    return this.http.get<Course>(`${this.API_URL}/${id}`, { headers: this.headers });
  }

  getCourseQuestions(courseId: number): Observable<Question[]> {
    return this.http.get<Question[]>(`http://localhost:8080/api/questions/course/${courseId}`, { headers: this.headers });
  }
}
