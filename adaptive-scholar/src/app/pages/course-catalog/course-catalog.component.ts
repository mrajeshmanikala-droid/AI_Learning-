import { Component, inject, OnInit, ChangeDetectorRef, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CourseService, Course } from '../../services/course.service';
import { SearchService } from '../../services/search.service';
import { ProgressService } from '../../services/progress.service';

@Component({
  selector: 'app-course-catalog',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './course-catalog.component.html'
})
export class CourseCatalogComponent implements OnInit {
  private courseService = inject(CourseService);
  private searchService = inject(SearchService);
  private progressService = inject(ProgressService);
  private cdr = inject(ChangeDetectorRef);

  courses: Course[] = [];
  filteredCourses: Course[] = [];
  isLoading = true;

  // Global search query from service
  searchQuery = this.searchService.query;

  // The featured course is usually the AI course
  featuredCourse: Course | null = null;

  constructor() {
    // React to search query changes automatically
    effect(() => {
      this.onSearch(this.searchQuery());
    });
  }

  ngOnInit() {
    this.loadCourses();
  }

  loadCourses() {
    this.isLoading = true;
    this.courseService.getCourses().subscribe({
      next: (data) => {
        this.courses = data;

        // Overlay user-specific progress
        this.progressService.getUserProgress().subscribe({
          next: (progressList) => {
            const progressMap = new Map<number, number>();
            for (const p of progressList) {
              progressMap.set(p.courseId, p.progress);
            }
            for (const course of this.courses) {
              course.progress = progressMap.get(course.id) ?? 0;
            }
            this.finalizeCourses();
          },
          error: () => {
            // If not logged in or error, default to 0
            this.courses.forEach(c => c.progress = 0);
            this.finalizeCourses();
          }
        });
      },
      error: (err) => {
        console.error('Failed to load courses in catalog:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private finalizeCourses(): void {
    // Pick the AI course as the featured hero card
    this.featuredCourse = this.courses.find(c => c.title.toLowerCase().includes('ai')) || this.courses[0] || null;
    // Everything else goes in the grid
    this.filteredCourses = this.courses.filter(c => c.id !== this.featuredCourse?.id);
    
    // Manual override for AI course image (only for courses catalog as requested)
    [this.featuredCourse, ...this.filteredCourses].forEach(c => {
      if (c && c.title.toLowerCase().includes('ai')) {
        c.image = 'https://lh3.googleusercontent.com/aida-public/AB6AXuDQ7kcCP18eqnMAbip1l7869KEMNHrNf3QskBD-FN5wvP-qKP8WivgeZoGCNOJX5yOs4RqhxEDzEG6VSS6hVNe9hrehbs32PYYqpq8aS8EcvtomuKQ_OgALO9yyjmmCneQYN-AxT-f64H-06KGxBckw-4-itGe1xXvssTKkoUX0sew_-TJ45T85eX-FaCWtoKAMyhHu7Wb_TAak9B-lRq3IoVHVgd2gILmjkYDeyk660jRp4ZZ_okYELY4a7Vn6cMXoMZgurNElPEc';
      }
    });

    this.isLoading = false;
    this.cdr.detectChanges();
  }

  onSearch(query: string) {
    const q = query.toLowerCase().trim();
    if (!q) {
      this.filteredCourses = this.courses.filter(c => c.id !== this.featuredCourse?.id);
    } else {
      // Search through ALL courses
      this.filteredCourses = this.courses.filter(c =>
        c.title.toLowerCase().includes(q) ||
        c.description.toLowerCase().includes(q)
      );
    }
    this.cdr.detectChanges();
  }

  getLessonCount(course: Course): number {
    return course.lessons?.length || 0;
  }
}
