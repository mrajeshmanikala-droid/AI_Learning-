import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CourseService, Course } from '../../services/course.service';
import { GeminiService } from '../../services/gemini.service';
import { ProgressService, UserProgress } from '../../services/progress.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private authService = inject(AuthService);
  private courseService = inject(CourseService);
  private aiService = inject(GeminiService);
  private progressService = inject(ProgressService);
  private cdr = inject(ChangeDetectorRef);

  courses: Course[] = [];
  isLoading = true;

  // Computed stats — all derived from USER-SPECIFIC progress
  masteryScore = 0;
  avgProgress = 0;
  totalLessons = 0;
  totalModules = 0;
  weeklyHours = '0h';
  consistency = 0;

  // AI insight
  aiInsight = '';
  aiInsightLoading = true;

  // Milestones
  milestoneLevel: 'onboarding' | 'basics' | 'intermediate' | 'advanced' | 'mastery' = 'onboarding';

  // Recent activity - derived from courses
  recentActivities: { icon: string; iconBg: string; iconColor: string; title: string; time: string; detail: string }[] = [];

  // Recommendations - derived from courses
  recommendations: { title: string; subtitle: string; courseId: number }[] = [];

  ngOnInit(): void {
    this.courseService.getCourses().subscribe({
      next: (data) => {
        this.courses = data;

        // If user is logged in, overlay their personal progress
        if (this.authService.currentUser) {
          this.loadUserProgress();
        } else {
          // Not logged in — show 0 progress for everything
          this.courses.forEach(c => c.progress = 0);
          this.finalizeDashboard();
        }
      },
      error: (err) => {
        console.error('Failed to load courses', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private loadUserProgress(): void {
    this.progressService.getUserProgress().subscribe({
      next: (progressList: UserProgress[]) => {
        // Build a map: courseId -> user's progress
        const progressMap = new Map<number, UserProgress>();
        for (const p of progressList) {
          progressMap.set(p.courseId, p);
        }

        // Override each course's global progress with the user's actual progress
        for (const course of this.courses) {
          const userProg = progressMap.get(course.id);
          if (userProg) {
            course.progress = userProg.progress;
          } else {
            // User has never touched this course — 0% progress
            course.progress = 0;
          }
        }

        this.finalizeDashboard();
      },
      error: (err) => {
        console.error('Failed to load user progress, defaulting to 0', err);
        // On error, default all progress to 0 for safety
        this.courses.forEach(c => c.progress = 0);
        this.finalizeDashboard();
      }
    });
  }

  private finalizeDashboard(): void {
    this.isLoading = false;
    this.computeStats();
    this.buildRecentActivity();
    this.buildRecommendations();
    this.generateAiInsight();
    this.cdr.detectChanges();
  }

  private computeStats(): void {
    if (this.courses.length === 0) return;

    // Only count courses the user has actually started for progress/mastery stats
    const startedCourses = this.courses.filter(c => c.progress > 0);

    // Average progress across STARTED courses only (per-user)
    if (startedCourses.length > 0) {
      const totalProgress = startedCourses.reduce((sum, c) => sum + c.progress, 0);
      this.avgProgress = Math.round(totalProgress / startedCourses.length);
    } else {
      this.avgProgress = 0;
    }

    // Total lessons and modules across ALL courses (catalog info)
    this.totalLessons = this.courses.reduce((sum, c) => sum + (c.lessons?.length || 0), 0);
    this.totalModules = this.courses.reduce((sum, c) => sum + (c.modulesCount || 0), 0);

    // Lessons completed by THIS user (from started courses only)
    const userLessons = startedCourses.reduce((sum, c) => sum + (c.lessons?.length || 0), 0);
    const completedLessons = Math.round(userLessons * (this.avgProgress / 100));

    // Mastery score: weighted by actual user engagement
    this.masteryScore = completedLessons * 40 + startedCourses.length * 200;

    // Weekly hours estimate (based on completed lessons)
    const estimatedHours = Math.round(completedLessons * 0.75);
    this.weeklyHours = `${estimatedHours}h`;

    // Consistency based on avg progress of started courses
    this.consistency = this.avgProgress;

    // Milestone level based on avg progress
    if (this.avgProgress >= 90) this.milestoneLevel = 'mastery';
    else if (this.avgProgress >= 70) this.milestoneLevel = 'advanced';
    else if (this.avgProgress >= 40) this.milestoneLevel = 'intermediate';
    else if (this.avgProgress >= 15) this.milestoneLevel = 'basics';
    else this.milestoneLevel = 'onboarding';
  }

  private buildRecentActivity(): void {
    this.recentActivities = [];

    for (const course of this.courses) {
      if (course.lessons && course.lessons.length > 0 && course.progress > 0) {
        // Latest lesson watched
        const latestLesson = course.lessons[Math.min(Math.floor(course.lessons.length * course.progress / 100), course.lessons.length - 1)];
        this.recentActivities.push({
          icon: 'play_circle',
          iconBg: 'bg-indigo-50',
          iconColor: 'text-indigo-600',
          title: 'Lesson Watched',
          time: 'Recently',
          detail: `${latestLesson.title}`
        });
      }

      if (course.progress > 50) {
        this.recentActivities.push({
          icon: 'trending_up',
          iconBg: 'bg-green-50',
          iconColor: 'text-green-600',
          title: 'Progress Milestone',
          time: 'This week',
          detail: `${course.progress}% complete in ${course.title}`
        });
      }
    }

    // Keep max 3
    this.recentActivities = this.recentActivities.slice(0, 3);

    if (this.recentActivities.length === 0) {
      this.recentActivities.push({
        icon: 'school',
        iconBg: 'bg-amber-50',
        iconColor: 'text-amber-600',
        title: 'Welcome!',
        time: 'Today',
        detail: 'Start your first course to see activity here.'
      });
    }
  }

  private buildRecommendations(): void {
    this.recommendations = [];
    // Recommend courses with lower progress first
    const sorted = [...this.courses].sort((a, b) => a.progress - b.progress);
    for (const c of sorted.slice(0, 2)) {
      if (c.lessons && c.lessons.length > 0) {
        const nextLessonIdx = Math.min(Math.floor(c.lessons.length * c.progress / 100), c.lessons.length - 1);
        const nextLesson = c.lessons[nextLessonIdx];
        this.recommendations.push({
          title: nextLesson ? nextLesson.title : c.title,
          subtitle: `Continue ${c.title}`,
          courseId: c.id
        });
      }
    }
  }

  private generateAiInsight(): void {
    if (this.courses.length === 0) {
      this.aiInsight = 'Start your first course to get personalized AI insights!';
      this.aiInsightLoading = false;
      return;
    }

    const courseInfo = this.courses.map(c => `"${c.title}" (${c.progress}% complete, ${c.lessons?.length || 0} lessons)`).join(', ');
    const prompt = `You are Scholar AI. The student is enrolled in: ${courseInfo}. Their average progress is ${this.avgProgress}%. Give ONE short, specific, personalized learning insight or recommendation in 2 sentences max. Be encouraging but specific about what they should focus on next. Do NOT use markdown formatting.`;

    this.aiService.sendMessage(prompt).subscribe({
      next: (response) => {
        this.aiInsight = response;
        this.aiInsightLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.aiInsight = 'Keep pushing forward! Focus on completing your current modules before moving to new ones.';
        this.aiInsightLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  get user() {
    return this.authService.currentUser;
  }

  get firstName() {
    const name = this.user?.name;
    return name ? name.split(' ')[0] : 'Scholar';
  }

  get strokeDashoffset(): number {
    // Circle circumference = 2*PI*76 ≈ 477.5
    return 477.5 - (477.5 * this.consistency / 100);
  }

  getMilestoneState(level: string): 'completed' | 'active' | 'locked' {
    const order = ['onboarding', 'basics', 'intermediate', 'advanced', 'mastery'];
    const currentIdx = order.indexOf(this.milestoneLevel);
    const levelIdx = order.indexOf(level);
    if (levelIdx < currentIdx) return 'completed';
    if (levelIdx === currentIdx) return 'active';
    return 'locked';
  }
}
