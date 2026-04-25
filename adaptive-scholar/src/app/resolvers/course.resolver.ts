import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { CourseService, Course } from '../services/course.service';
import { Observable } from 'rxjs';

export const courseResolver: ResolveFn<Course[]> = (): Observable<Course[]> => {
  const courseService = inject(CourseService);
  return courseService.getCourses();
};
