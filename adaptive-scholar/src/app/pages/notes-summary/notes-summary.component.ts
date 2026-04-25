import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GeminiService } from '../../services/gemini.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-notes-summary',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notes-summary.component.html'
})
export class NotesSummaryComponent {
  private aiService = inject(GeminiService);
  private cdr = inject(ChangeDetectorRef);

  inputText = '';
  isLoading = false;
  hasResult = false;

  // AI result fields
  coreSummary = '';
  highlights: string[] = [];
  scholarPerspective = '';
  taxonomy = '';
  estReview = '';

  // PDF handling
  pdfFileName = '';

  onFileSelect(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    if (file.type !== 'application/pdf' && !file.name.endsWith('.pdf')) {
      alert('Please upload a PDF file.');
      return;
    }

    this.pdfFileName = file.name;
    const reader = new FileReader();
    reader.onload = () => {
      // Extract readable text from PDF via basic text layer
      const text = reader.result as string;
      // For PDF, we'll send the filename context + any extractable text
      this.inputText = `[Uploaded PDF: ${file.name}]\n\n${this.extractTextFromDataUri(text)}`;
      this.cdr.detectChanges();
    };
    reader.readAsText(file);
  }

  private extractTextFromDataUri(raw: string): string {
    // Basic extraction: strip binary noise, keep readable ASCII
    return raw.replace(/[^\x20-\x7E\n\r\t]/g, ' ')
              .replace(/\s{3,}/g, '\n')
              .substring(0, 8000); // Limit context size
  }

  generateSummary(): void {
    if (!this.inputText.trim() || this.isLoading) return;

    this.isLoading = true;
    this.hasResult = false;

    const prompt = `Analyze the following study material and return a structured JSON response with EXACTLY this format (no markdown fences, just raw JSON):
{
  "coreSummary": "A 2-3 sentence summary of the core concepts",
  "highlights": ["highlight 1", "highlight 2", "highlight 3", "highlight 4", "highlight 5"],
  "scholarPerspective": "A thoughtful 1-2 sentence insight connecting this material to broader learning patterns",
  "taxonomy": "The primary subject/topic category (e.g. Machine Learning, Web Development, etc.)",
  "estReview": "Estimated review time like '3 Minutes' or '5 Minutes'"
}

Study Material:
${this.inputText.substring(0, 6000)}`;

    this.aiService.sendMessage(prompt).subscribe({
      next: (response) => {
        try {
          // Try to extract JSON from the response
          const jsonMatch = response.match(/\{[\s\S]*\}/);
          if (jsonMatch) {
            const parsed = JSON.parse(jsonMatch[0]);
            this.coreSummary = parsed.coreSummary || 'No summary generated.';
            this.highlights = parsed.highlights || [];
            this.scholarPerspective = parsed.scholarPerspective || '';
            this.taxonomy = parsed.taxonomy || 'General';
            this.estReview = parsed.estReview || '3 Minutes';
          } else {
            // Fallback: use the raw text as summary
            this.coreSummary = response;
            this.highlights = [];
            this.scholarPerspective = '';
            this.taxonomy = 'General';
            this.estReview = '3 Minutes';
          }
        } catch (e) {
          this.coreSummary = response;
          this.highlights = [];
          this.scholarPerspective = '';
          this.taxonomy = 'General';
          this.estReview = '3 Minutes';
        }
        this.hasResult = true;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('AI Summary Error:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }
}
