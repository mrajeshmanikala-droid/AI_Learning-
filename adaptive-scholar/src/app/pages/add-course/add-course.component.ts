import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

interface LessonForm {
  title: string;
  description: string;
  videoUrl: string;
  duration: string;
  module: string;
}

@Component({
  selector: 'app-add-course',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './add-course.component.html'
})
export class AddCourseComponent {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private router = inject(Router);

  title = '';
  description = '';
  image = '';
  modulesCount = 1;
  
  lessons: LessonForm[] = [
    { title: '', description: '', videoUrl: '', duration: '', module: '' }
  ];

  isSubmitting = false;

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
      image: this.image || 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&q=80&w=1000',
      modulesCount: this.modulesCount,
      progress: 0,
      lessons: this.lessons
    };

    const headers = this.authService.getAuthHeaders();
    this.http.post('http://localhost:8080/api/courses', courseData, { headers }).subscribe({
      next: () => {
        this.router.navigate(['/admin/manage-courses']);
      },
      error: (err) => {
        console.error('Error adding course', err);
        this.isSubmitting = false;
        alert('Failed to add course. Please check your inputs and permissions.');
      }
    });
  }
}
