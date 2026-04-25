import { Component, inject, ViewChild, ElementRef, AfterViewChecked, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { GeminiService, ChatMessage } from '../../services/gemini.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-ai-assistant',
  imports: [CommonModule, FormsModule],
  templateUrl: './ai-assistant.component.html'
})
export class AiAssistantComponent implements OnInit, AfterViewChecked {
  private geminiService = inject(GeminiService);
  private authService = inject(AuthService);
  private sanitizer = inject(DomSanitizer);
  private cdr = inject(ChangeDetectorRef);

  @ViewChild('chatContainer') chatContainer!: ElementRef;
  @ViewChild('messageInput') messageInput!: ElementRef;

  messages: ChatMessage[] = [];
  userInput = '';
  isLoading = false;
  Math = Math; // expose Math to template

  private shouldScroll = false;
  
  private get messageKey(): string {
    const email = this.authService.currentUser?.email;
    return `scholar_ui_messages_${email || 'anonymous'}`;
  }

  get firstName(): string {
    const name = this.authService.currentUser?.name;
    return name ? name.split(' ')[0] : 'Scholar';
  }

  ngOnInit(): void {
    this.loadUiHistory();
    if (this.messages.length === 0) {
      this.messages.push({
        role: 'ai',
        content: `Welcome back, **${this.firstName}**! 👋\n\nI'm your intelligent learning partner. I can help you with:\n\n- **Explaining concepts** in any subject\n- **Generating quizzes** to test your understanding\n- **Summarizing notes** and lectures\n- **Deep dives** into complex topics\n\nWhat would you like to explore today?`,
        timestamp: new Date()
      });
      this.saveUiHistory();
    }
  }

  private loadUiHistory(): void {
    const saved = sessionStorage.getItem(this.messageKey);
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        this.messages = parsed.map((m: any) => ({
          ...m,
          timestamp: new Date(m.timestamp)
        }));
      } catch (e) {
        console.error('Failed to load UI history', e);
      }
    }
  }

  private saveUiHistory(): void {
    sessionStorage.setItem(this.messageKey, JSON.stringify(this.messages));
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  sendMessage(): void {
    const text = this.userInput.trim();
    if (!text || this.isLoading) return;

    this.messages.push({
      role: 'user',
      content: text,
      timestamp: new Date()
    });
    this.saveUiHistory();

    this.userInput = '';
    this.isLoading = true;
    this.shouldScroll = true;

    if (this.messageInput?.nativeElement) {
      this.messageInput.nativeElement.style.height = '56px';
    }

    this.geminiService.sendMessage(text).subscribe({
      next: (response) => {
        this.messages.push({
          role: 'ai',
          content: response,
          timestamp: new Date()
        });
        this.saveUiHistory();
        this.isLoading = false;
        this.shouldScroll = true;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Gemini API error (caught in component):', err);
        const errorMessage = err?.message || 'I apologize, but I encountered an issue processing your request. Please try again in a moment. (Rate Limit)';
        this.messages.push({
          role: 'ai',
          content: errorMessage,
          timestamp: new Date()
        });
        this.saveUiHistory();
        this.isLoading = false;
        this.shouldScroll = true;
        this.cdr.detectChanges();
      }
    });
  }

  sendQuickAction(action: string): void {
    this.userInput = action;
    this.sendMessage();
  }

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  autoResize(event: Event): void {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = '56px';
    textarea.style.height = Math.min(textarea.scrollHeight, 128) + 'px';
  }

  formatTime(date: Date): string {
    return date.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true });
  }

  formatMarkdown(text: string): SafeHtml {
    let html = text;
    
    // 1. Extract Code Blocks to protect them from other formatting
    const codeBlocks: string[] = [];
    html = html.replace(/```(\w*)\n?([\s\S]*?)```/g, (_match, lang, code) => {
      const placeholder = `__CODEBLOCK_${codeBlocks.length}__`;
      codeBlocks.push(`<pre><code>${this.escapeHtml(code.trim())}</code></pre>`);
      return placeholder;
    });

    const inlineCodes: string[] = [];
    html = html.replace(/`([^`]+)`/g, (_match, code) => {
      const placeholder = `__INLINECODE_${inlineCodes.length}__`;
      inlineCodes.push(`<code>${this.escapeHtml(code)}</code>`);
      return placeholder;
    });

    // 2. Format remaining text
    html = html.replace(/\*\*(.+?)\*\*/g, '<strong class="font-bold text-on-surface">$1</strong>');
    html = html.replace(/\*(.+?)\*/g, '<em>$1</em>');
    html = html.replace(/^- (.+)$/gm, '<li class="ml-4 list-disc text-on-surface-variant">$1</li>');
    html = html.replace(/(<li[^>]*>.*<\/li>\n?)+/g, '<ul class="space-y-1 my-2">$&</ul>');
    html = html.replace(/^\d+\. (.+)$/gm, '<li class="ml-4 list-decimal text-on-surface-variant">$1</li>');
    html = html.replace(/^### (.+)$/gm, '<h4 class="font-headline font-bold text-base mt-4 mb-2 text-on-surface">$1</h4>');
    html = html.replace(/^## (.+)$/gm, '<h3 class="font-headline font-bold text-lg mt-4 mb-2 text-on-surface">$1</h3>');

    // Paragraph handling
    html = html.replace(/\n\n/g, '</p><p class="text-on-surface-variant leading-relaxed mb-2">');
    html = html.replace(/\n/g, '<br/>');

    if (!html.startsWith('<')) {
      html = `<p class="text-on-surface-variant leading-relaxed mb-2">${html}</p>`;
    }

    // 3. Re-inject Code Blocks
    html = html.replace(/__INLINECODE_(\d+)__/g, (_match, index) => inlineCodes[Number(index)]);
    html = html.replace(/__CODEBLOCK_(\d+)__/g, (_match, index) => codeBlocks[Number(index)]);

    return this.sanitizer.bypassSecurityTrustHtml(html);
  }

  clearChat(): void {
    this.geminiService.clearHistory();
    sessionStorage.removeItem(this.messageKey);
    this.messages = [];
    this.ngOnInit();
  }

  private escapeHtml(text: string): string {
    const map: Record<string, string> = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' };
    return text.replace(/[&<>"']/g, m => map[m]);
  }

  private scrollToBottom(): void {
    try {
      const el = this.chatContainer?.nativeElement;
      if (el) {
        el.scrollTop = el.scrollHeight;
      }
    } catch (err) {}
  }
}
