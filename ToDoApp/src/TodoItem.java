import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ToDoアイテムを表すクラス
 */
public class TodoItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    
    /**
     * コンストラクタ
     * 
     * @param title ToDoのタイトル
     * @param description ToDoの詳細説明
     */
    public TodoItem(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.completedAt = null;
    }
    
    // ゲッターとセッター
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed) {
            this.completedAt = LocalDateTime.now();
        } else {
            this.completedAt = null;
        }
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    /**
     * 作成日時を文字列形式で取得
     * 
     * @return フォーマットされた作成日時
     */
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return createdAt.format(formatter);
    }
    
    /**
     * 完了日時を文字列形式で取得
     * 
     * @return フォーマットされた完了日時、未完了の場合は空文字列
     */
    public String getFormattedCompletedAt() {
        if (completedAt == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return completedAt.format(formatter);
    }
    
    @Override
    public String toString() {
        return title + (completed ? " (完了)" : " (未完了)");
    }
}