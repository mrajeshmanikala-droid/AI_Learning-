import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-manage-courses',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './manage-courses.component.html'
})
export class ManageCoursesComponent implements OnInit {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  courses: any[] = [];
  isLoading = true;

  ngOnInit() {
    this.fetchCourses();
  }

  fetchCourses() {
    this.http.get<any[]>('http://localhost:8080/api/courses').subscribe({
      next: (data) => {
        this.courses = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching courses', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  deleteCourse(id: number) {
    if (confirm('Are you absolutely sure? This will permanently delete the course and all its lessons.')) {
      const headers = this.authService.getAuthHeaders();
      this.http.delete(`http://localhost:8080/api/courses/${id}`, { headers }).subscribe({
        next: () => {
          this.courses = this.courses.filter(c => c.id !== id);
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error deleting course', err);
          alert('Delete failed. Ensure you have administrative authority.');
          this.cdr.detectChanges();
        }
      });
    }
  }
}
