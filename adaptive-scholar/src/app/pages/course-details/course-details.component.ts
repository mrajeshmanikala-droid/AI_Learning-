import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { CourseService, Course, Lesson } from '../../services/course.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GeminiService } from '../../services/gemini.service';
import { ProgressService } from '../../services/progress.service';

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './course-details.component.html',
  styles: [`
    @keyframes chatExpand {
      from { transform: scale(0.5) translateY(20px); opacity: 0; }
      to { transform: scale(1) translateY(0); opacity: 1; }
    }
    .animate-chat-expand {
      animation: chatExpand 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
    }
  `]
})
export class CourseDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private courseService = inject(CourseService);
  private sanitizer = inject(DomSanitizer);
  private cdr = inject(ChangeDetectorRef);
  private geminiService = inject(GeminiService);
  private progressService = inject(ProgressService);

  course: Course | undefined;
  activeLessonIndex = 0;
  
  // Chat state
  isChatOpen = false;
  chatMessages: { role: 'user' | 'ai', content: string }[] = [];
  newChatMessage = '';
  isChatLoading = false;

  // Download state
  isDownloading = false;

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const courseId = Number(params.get('id'));
      if (courseId) {
        this.activeLessonIndex = 0;
        this.courseService.getCourseById(courseId).subscribe({
          next: (data) => {
            this.course = data;
            // Manual override for AI course image (only for courses route as requested)
            if (this.course && this.course.title.toLowerCase().includes('ai')) {
              this.course.image = 'https://lh3.googleusercontent.com/aida-public/AB6AXuDQ7kcCP18eqnMAbip1l7869KEMNHrNf3QskBD-FN5wvP-qKP8WivgeZoGCNOJX5yOs4RqhxEDzEG6VSS6hVNe9hrehbs32PYYqpq8aS8EcvtomuKQ_OgALO9yyjmmCneQYN-AxT-f64H-06KGxBckw-4-itGe1xXvssTKkoUX0sew_-TJ45T85eX-FaCWtoKAMyhHu7Wb_TAak9B-lRq3IoVHVgd2gILmjkYDeyk660jRp4ZZ_okYELY4a7Vn6cMXoMZgurNElPEc';
            }

            // Load existing progress to resume where the user left off
            this.progressService.getUserProgress().subscribe({
              next: (progressList) => {
                const myProgress = progressList.find(p => p.courseId === courseId);
                if (myProgress && myProgress.completedLessons > 0) {
                  // Resume from the last lesson they reached
                  this.activeLessonIndex = Math.min(myProgress.completedLessons - 1, this.course!.lessons.length - 1);
                } else {
                  // First time opening — save initial progress (lesson 1)
                  this.saveProgress();
                }
                this.cdr.detectChanges();
              },
              error: () => {
                // Can't load progress — just save current position
                this.saveProgress();
                this.cdr.detectChanges();
              }
            });

            this.cdr.detectChanges();
          },
          error: (err) => console.error('[CourseDetails] API ERROR:', err)
        });
      }
    });
  }

  get activeLesson(): Lesson | undefined {
    return this.course?.lessons[this.activeLessonIndex];
  }

  get groupedLessons() {
    if (!this.course) return [];
    const groups: { name: string, lessons: { data: Lesson, index: number }[] }[] = [];
    
    this.course.lessons.forEach((lesson, index) => {
      let group = groups.find(g => g.name === lesson.module);
      if (!group) {
        group = { name: lesson.module, lessons: [] };
        groups.push(group);
      }
      group.lessons.push({ data: lesson, index });
    });
    
    return groups;
  }

  getSafeVideoUrl(videoUrl: string): SafeResourceUrl {
    const videoId = this.extractVideoId(videoUrl);
    return this.sanitizer.bypassSecurityTrustResourceUrl(`https://www.youtube.com/embed/${videoId}?autoplay=1&rel=0`);
  }

  /**
   * Extracts a YouTube video ID from various URL formats:
   * - https://www.youtube.com/watch?v=VIDEO_ID
   * - https://youtu.be/VIDEO_ID
   * - https://www.youtube.com/embed/VIDEO_ID
   * - or just a plain VIDEO_ID string
   */
  private extractVideoId(url: string): string {
    if (!url) return '';
    // Already a plain video ID (no slashes, no dots)
    if (/^[A-Za-z0-9_-]{11}$/.test(url.trim())) {
      return url.trim();
    }
    try {
      const parsed = new URL(url);
      // youtube.com/watch?v=ID
      if (parsed.searchParams.has('v')) {
        return parsed.searchParams.get('v') || '';
      }
      // youtu.be/ID or youtube.com/embed/ID
      const segments = parsed.pathname.split('/').filter(Boolean);
      if (segments.length > 0) {
        return segments[segments.length - 1];
      }
    } catch {
      // Not a valid URL — treat the whole string as a video ID
      return url.trim();
    }
    return url.trim();
  }

  setActiveLesson(index: number): void {
    this.activeLessonIndex = index;
    this.chatMessages = [];
    this.saveProgress();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  nextLesson(): void {
    if (this.course && this.activeLessonIndex < this.course.lessons.length - 1) {
      this.activeLessonIndex++;
      this.chatMessages = [];
      this.saveProgress();
    }
  }

  prevLesson(): void {
    if (this.activeLessonIndex > 0) {
      this.activeLessonIndex--;
      this.chatMessages = [];
      this.saveProgress();
    }
  }

  private saveProgress(): void {
    if (!this.course) return;
    const totalLessons = this.course.lessons.length;
    if (totalLessons === 0) return;

    // The user has "reached" lesson at activeLessonIndex + 1
    const completedLessons = this.activeLessonIndex + 1;
    const progressPct = Math.round((completedLessons / totalLessons) * 100);

    this.progressService.updateProgress(this.course.id, progressPct, completedLessons).subscribe({
      next: () => {},
      error: (err) => console.error('Failed to save progress', err)
    });
  }

  toggleChat(): void {
    this.isChatOpen = !this.isChatOpen;
    if (this.isChatOpen && this.chatMessages.length === 0 && this.activeLesson) {
      const prompt = `Can you explain the main idea of "${this.activeLesson.title}" very simply?`;
      this.chatMessages.push({ role: 'user', content: prompt });
      this.isChatLoading = true;
      this.geminiService.sendMessage(prompt).subscribe({
        next: (response) => {
          this.chatMessages.push({ role: 'ai', content: response });
          this.isChatLoading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error(err);
          this.isChatLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  sendMessage(): void {
    if (!this.newChatMessage.trim() || this.isChatLoading) return;
    
    const message = this.newChatMessage;
    this.newChatMessage = '';
    this.chatMessages.push({ role: 'user', content: message });
    this.isChatLoading = true;
    
    this.geminiService.sendMessage(message).subscribe({
      next: (response) => {
        this.chatMessages.push({ role: 'ai', content: response });
        this.isChatLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.isChatLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  downloadCheatsheet(): void {
    if (!this.activeLesson || this.isDownloading) return;
    this.isDownloading = true;

    const prompt = `Create a concise, well-structured cheatsheet for the topic: "${this.activeLesson.title}".
Format it as clean plain text with sections using === headers ===, bullet points with -, and code examples wrapped in backticks.
Include: Key Concepts, Important Syntax/Commands, Common Patterns, Quick Tips, and Common Mistakes to Avoid.
Keep it under 800 words. Do NOT use markdown — use plain text formatting only.`;

    this.geminiService.sendMessage(prompt).subscribe({
      next: (content) => {
        const lessonTitle = this.activeLesson!.title;
        const htmlContent = `<!DOCTYPE html>
<html><head><meta charset="UTF-8">
<title>Cheatsheet - ${lessonTitle}</title>
<style>
  body { font-family: 'Segoe UI', system-ui, sans-serif; max-width: 800px; margin: 40px auto; padding: 20px; color: #1a1a2e; line-height: 1.7; }
  h1 { color: #4b41e1; border-bottom: 3px solid #4b41e1; padding-bottom: 12px; font-size: 24px; }
  pre { background: #f5f3ff; padding: 16px; border-radius: 8px; border-left: 4px solid #4b41e1; overflow-x: auto; font-size: 13px; white-space: pre-wrap; word-wrap: break-word; }
  .meta { color: #666; font-size: 12px; margin-bottom: 24px; }
  .footer { margin-top: 40px; padding-top: 16px; border-top: 1px solid #eee; color: #999; font-size: 11px; text-align: center; }
</style></head><body>
<h1>📋 ${lessonTitle}</h1>
<p class="meta">Generated by Scholar AI • ${new Date().toLocaleDateString()}</p>
<pre>${content.replace(/</g, '&lt;').replace(/>/g, '&gt;')}</pre>
<div class="footer">The Adaptive Scholar — AI-Powered Education Platform</div>
</body></html>`;

        const blob = new Blob([htmlContent], { type: 'text/html' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${lessonTitle.replace(/[^a-zA-Z0-9]/g, '_')}_Cheatsheet.html`;
        a.click();
        URL.revokeObjectURL(url);
        this.isDownloading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.isDownloading = false;
        this.cdr.detectChanges();
      }
    });
  }

  downloadSourceRef(): void {
    if (!this.activeLesson) return;
    const content = `// ===================================================
// Source Code Reference: ${this.activeLesson.title}
// Course: ${this.course?.title || 'Adaptive Scholar'}
// Generated: ${new Date().toLocaleDateString()}
// ===================================================

/*
  This file contains code references for the lesson:
  "${this.activeLesson.title}"

  Description:
  ${this.activeLesson.description}

  Watch the full video lesson on The Adaptive Scholar platform
  to see these concepts implemented step-by-step.

  Module: ${this.activeLesson.module}
  Duration: ${this.activeLesson.duration}
*/

// Key topics covered in this lesson:
// - Review the video at https://youtube.com/watch?v=${this.extractVideoId(this.activeLesson.videoUrl)}
// - Practice the concepts in your own development environment
// - Use the AI Assistant for any clarifications

// Happy coding! 🚀
`;
    const blob = new Blob([content], { type: 'text/plain' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${this.activeLesson.title.replace(/[^a-zA-Z0-9]/g, '_')}_Reference.txt`;
    a.click();
    URL.revokeObjectURL(url);
  }
}
