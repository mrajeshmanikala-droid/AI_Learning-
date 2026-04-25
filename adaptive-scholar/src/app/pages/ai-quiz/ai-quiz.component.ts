import { Component, OnInit, OnDestroy, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CourseService, Course, Question } from '../../services/course.service';
import { GeminiService } from '../../services/gemini.service';

@Component({
  selector: 'app-ai-quiz',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './ai-quiz.component.html'
})
export class AiQuizComponent implements OnInit, OnDestroy {
  private route = inject(ActivatedRoute);
  private courseService = inject(CourseService);
  private aiService = inject(GeminiService);
  private cdr = inject(ChangeDetectorRef);

  course: Course | null = null;
  questions: Question[] = [];
  selectedAnswers: { [questionId: number]: number } = {};
  isSubmitted = false;
  score = 0;
  isLoading = true;
  isAiGenerated = false;
  Math = Math;
  
  timeRemaining = 900;
  timerInterval: any;
  formattedTime = '15:00';

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (id) {
        this.loadQuizData(id);
      }
    });
  }

  ngOnDestroy() {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
  }

  startTimer() {
    this.timerInterval = setInterval(() => {
      if (this.isSubmitted) {
        clearInterval(this.timerInterval);
        return;
      }
      if (this.timeRemaining > 0) {
        this.timeRemaining--;
        const minutes = Math.floor(this.timeRemaining / 60);
        const seconds = this.timeRemaining % 60;
        this.formattedTime = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        this.cdr.detectChanges();
      } else {
        clearInterval(this.timerInterval);
        this.submitAssessment();
      }
    }, 1000);
  }

  loadQuizData(courseId: number) {
    this.courseService.getCourseById(courseId).subscribe({
      next: (course) => {
        this.course = course;
        this.cdr.detectChanges();
        // Try AI-generated questions first, fall back to static DB questions
        this.generateAiQuestions(course, courseId);
      },
      error: (err) => {
        console.error('Failed to load course', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private generateAiQuestions(course: Course, courseId: number) {
    const prompt = `Generate exactly 10 multiple-choice quiz questions for a course titled "${course.title}".
${course.description ? 'Course description: ' + course.description : ''}

Return ONLY a valid JSON array with no markdown fences, no explanation, just the raw JSON array. Each object must have exactly this structure:
[
  {
    "text": "Question text here?",
    "options": ["Option A", "Option B", "Option C", "Option D"],
    "correctOptionIndex": 0,
    "explanation": "Brief explanation of why this is correct."
  }
]

Requirements:
- Exactly 10 questions
- Each question must have exactly 4 options
- correctOptionIndex must be 0, 1, 2, or 3
- Questions should vary in difficulty from beginner to advanced
- Make them educational and relevant to the course topic
- Do NOT repeat questions, each should test a different concept`;

    this.aiService.sendMessage(prompt).subscribe({
      next: (response) => {
        try {
          const jsonMatch = response.match(/\[[\s\S]*\]/);
          if (jsonMatch) {
            const parsed = JSON.parse(jsonMatch[0]);
            if (Array.isArray(parsed) && parsed.length > 0) {
              // Assign synthetic IDs and validate structure
              this.questions = parsed.map((q: any, index: number) => ({
                id: 9000 + index,
                text: q.text || 'Question text missing',
                options: Array.isArray(q.options) && q.options.length === 4 ? q.options : ['A', 'B', 'C', 'D'],
                correctOptionIndex: typeof q.correctOptionIndex === 'number' ? q.correctOptionIndex : 0,
                explanation: q.explanation || 'No explanation provided.'
              }));
              this.isAiGenerated = true;
              this.isLoading = false;
              this.startTimer();
              this.cdr.detectChanges();
              console.log('[Quiz] AI-generated questions loaded successfully');
              return;
            }
          }
          // JSON parsing didn't yield valid array, fall back
          console.warn('[Quiz] AI response was not valid JSON array, falling back to static questions');
          this.fallbackToStatic(courseId);
        } catch (e) {
          console.warn('[Quiz] Failed to parse AI response, falling back to static questions', e);
          this.fallbackToStatic(courseId);
        }
      },
      error: (err) => {
        console.warn('[Quiz] AI generation failed, falling back to static questions', err);
        this.fallbackToStatic(courseId);
      }
    });
  }

  private fallbackToStatic(courseId: number) {
    this.courseService.getCourseQuestions(courseId).subscribe({
      next: (qs) => {
        this.questions = qs;
        this.isAiGenerated = false;
        this.isLoading = false;
        this.startTimer();
        this.cdr.detectChanges();
        console.log('[Quiz] Static questions loaded from database');
      },
      error: (err) => {
        console.error('Failed to load static questions', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  selectOption(questionId: number, optionIndex: number) {
    if (this.isSubmitted) return;
    this.selectedAnswers[questionId] = optionIndex;
  }

  submitAssessment() {
    if (this.isSubmitted || this.questions.length === 0) return;
    
    let correctCount = 0;
    for (const q of this.questions) {
      if (this.selectedAnswers[q.id] === q.correctOptionIndex) {
        correctCount++;
      }
    }
    
    this.score = Math.round((correctCount / this.questions.length) * 100);
    this.isSubmitted = true;
  }
}
