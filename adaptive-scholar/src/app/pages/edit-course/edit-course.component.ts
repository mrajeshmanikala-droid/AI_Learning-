import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

interface LessonForm {
  id?: number;
  title: string;
  description: string;
  videoUrl: string;
  duration: string;
  module: string;
}

@Component({
  selector: 'app-edit-course',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './edit-course.component.html'
})
export class EditCourseComponent implements OnInit {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  courseId: number | null = null;
  title = '';
  description = '';
  image = '';
  modulesCount = 1;
  
  lessons: LessonForm[] = [];

  isLoading = true;
  isSubmitting = false;

  ngOnInit() {
    this.courseId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.courseId) {
      this.fetchCourseData();
    }
  }

  fetchCourseData() {
    this.http.get<any>(`http://localhost:8080/api/courses/${this.courseId}`).subscribe({
      next: (course) => {
        this.title = course.title;
        this.description = course.description;
        this.image = course.image;
        this.modulesCount = course.modulesCount;
        this.lessons = course.lessons || [];
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching course', err);
        this.isLoading = false;
        this.cdr.detectChanges();
        alert('Could not find course to edit.');
        this.router.navigate(['/admin/manage-courses']);
      }
    });
  }

  addLesson() {
    this.lessons.push({ title: '', description: '', videoUrl: '', duration: '', module: '' });
  }

  removeLesson(index: number) {
    if (this.lessons.length > 1) {
      this.lessons.splice(index, 1);
    }
  }

  onSubmit() {
    this.isSubmitting = true;
    const courseData = {
      title: this.title,
      description: this.description,
      image: this.image,
      modulesCount: this.modulesCount,
      lessons: this.lessons
    };

    const headers = this.authService.getAuthHeaders();
    this.http.put(`http://localhost:8080/api/courses/${this.courseId}`, courseData, { headers }).subscribe({
      next: () => {
        this.router.navigate(['/admin/manage-courses']);
      },
      error: (err) => {
        console.error('Error updating course', err);
        this.isSubmitting = false;
        this.cdr.detectChanges();
        alert('Failed to update course.');
      }
    });
  }
}
