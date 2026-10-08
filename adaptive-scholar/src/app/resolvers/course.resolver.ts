import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { CourseService, Course } from '../services/course.service';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const courseResolver: ResolveFn<Course[]> = (): Observable<Course[]> => {
  const courseService = inject(CourseService);
  return courseService.getCourses().pipe(
    catchError((err) => {
      console.error('Course resolver failed, returning empty list:', err);
      return of([]);
    })
  );
};
