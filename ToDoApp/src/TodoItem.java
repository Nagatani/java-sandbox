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
     * @param title       ToDoのタイトル
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
        this.completedAt = completed ? LocalDateTime.now() : null; // 三項演算子で簡潔に
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
        return formatDateTime(createdAt); // メソッド抽出
    }

    /**
     * 完了日時を文字列形式で取得
     *
     * @return フォーマットされた完了日時、未完了の場合は空文字列
     */
    public String getFormattedCompletedAt() {
        return completedAt == null ? "" : formatDateTime(completedAt); // メソッド抽出
    }

    /**
     * 日時をフォーマットする共通メソッド
     *
     * @param dateTime フォーマットする日時
     * @return フォーマットされた日時文字列
     */
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return dateTime.format(formatter);
    }

    @Override
    public String toString() {
        return title + (completed ? " (完了)" : " (未完了)");
    }
}