import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
  private apiKey = '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';

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

  private conversationHistory: { role: string; content: string }[] = [];

  private get storageKey(): string {
    const email = this.authService.currentUser?.email;
    return `scholar_api_history_${email || 'anonymous'}`;
  }

  private apiUrl = 'https://api.groq.com/openai/v1/chat/completions';

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
    const messages = [
      { role: 'system', content: this.systemPrompt },
      ...this.conversationHistory,
      { role: 'user', content: userMessage }
    ];

    const requestBody = {
      model: 'llama-3.3-70b-versatile',
      messages,
      temperature: 0.7,
      top_p: 0.9,
      max_tokens: 2048
    };

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.apiKey}`,
      'Content-Type': 'application/json'
    });

    return this.http.post<any>(this.apiUrl, requestBody, { headers }).pipe(
      map(response => {
        const aiText = response?.choices?.[0]?.message?.content
          || 'I could not generate a response. Please try again.';

        this.conversationHistory.push(
          { role: 'user', content: userMessage },
          { role: 'assistant', content: aiText }
        );
        this.saveHistory();
        return aiText;
      }),
      catchError((error) => {
        console.error('Groq API Error:', error);

        let errorMsg = '⚠️ ';
        if (error.status === 429) {
          errorMsg += 'Rate limit reached. Groq allows 30 requests per minute on the free tier. Please wait a moment and try again.';
        } else if (error.status === 400 || error.status === 404) {
          errorMsg += 'Model configuration error. The requested AI model could not be found or processed.';
        } else if (error.status === 401 || error.status === 403) {
          errorMsg += 'API Key is invalid or unauthorized. Please check your Groq API key.';
        } else if (error.status === 413) {
          errorMsg += 'Context too long. Please clear the chat history and try again.';
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
