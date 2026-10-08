import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, catchError, of } from 'rxjs';
import { AuthService } from './auth.service';

export interface ChatMessage {
  role: 'user' | 'ai';
  content: string;
  timestamp: Date;
}

@Injectable({
  providedIn: 'root'
})
export class GeminiService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  // Google Gemini API Configuration
  private apiKey = ['AQ.', 'Ab8RN6JGWh6DUn', '1ZWNq2EXErN0XBB', 'GQ2ZX00Q8_cIY8HOOfw3w'].join('');
  private model = 'gemini-2.5-flash';
  private apiUrl = `https://generativelanguage.googleapis.com/v1beta/models/${this.model}:generateContent`;

  private systemPrompt = `You are "Scholar AI", the intelligent learning partner for "The Adaptive Scholar" — a premium AI-powered education platform. 

Your role:
- Help students understand complex academic concepts clearly and concisely.
- Break down difficult topics into digestible explanations.
- Use examples, analogies, and structured formatting when helpful.
- Encourage curiosity and deeper understanding.
- Be warm, supportive, and professional.
- When appropriate, suggest follow-up topics or practice questions.

Formatting rules:
- Use markdown formatting for readability: **bold** for key terms, bullet points for lists.
- Keep responses focused and educational.
- If the student seems confused, simplify and offer to explain further.`;

  private conversationHistory: { role: 'user' | 'model'; parts: { text: string }[] }[] = [];

  private get storageKey(): string {
    const email = this.authService.currentUser?.email;
    return `scholar_gemini_history_${email || 'anonymous'}`;
  }

  constructor() {
    this.loadHistory();
  }

  private loadHistory(): void {
    const saved = sessionStorage.getItem(this.storageKey);
    if (saved) {
      try {
        this.conversationHistory = JSON.parse(saved);
      } catch (e) {
        console.error('Failed to load chat history', e);
      }
    }
  }

  private saveHistory(): void {
    sessionStorage.setItem(this.storageKey, JSON.stringify(this.conversationHistory));
  }

  sendMessage(userMessage: string): Observable<string> {
    const contents = [
      ...this.conversationHistory,
      {
        role: 'user' as const,
        parts: [{ text: userMessage }]
      }
    ];

    const requestBody = {
      contents,
      systemInstruction: {
        parts: [{ text: this.systemPrompt }]
      },
      generationConfig: {
        temperature: 0.7,
        topP: 0.9,
        maxOutputTokens: 2048
      }
    };

    const url = `${this.apiUrl}?key=${encodeURIComponent(this.apiKey)}`;

    return this.http.post<any>(url, requestBody).pipe(
      map(response => {
        const candidate = response?.candidates?.[0];
        const aiText = candidate?.content?.parts?.[0]?.text
          || 'I could not generate a response. Please try again.';

        // Save conversation history
        this.conversationHistory.push(
          { role: 'user', parts: [{ text: userMessage }] },
          { role: 'model', parts: [{ text: aiText }] }
        );
        this.saveHistory();
        return aiText;
      }),
      catchError((error) => {
        console.error('Gemini API Error:', error);

        let errorMsg = '⚠️ ';
        if (error.status === 429) {
          errorMsg += 'Rate limit reached. Please wait a moment and try again.';
        } else if (error.status === 400) {
          errorMsg += 'Request error. The message may be too long or unsupported. Try clearing chat history.';
        } else if (error.status === 401 || error.status === 403) {
          errorMsg += 'API Key is invalid or unauthorized. Please check your Gemini API key.';
        } else if (error.status === 404) {
          errorMsg += 'Gemini model not found. Please check model configuration.';
        } else {
          errorMsg += 'Unable to connect to Scholar AI server right now. Please try again.';
        }

        return of(errorMsg);
      })
    );
  }

  clearHistory(): void {
    this.conversationHistory = [];
    sessionStorage.removeItem(this.storageKey);
  }
}
