import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ToDoアプリケーションのメインクラス
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    /**
     * アプリケーションのエントリーポイント
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        // 文字エンコーディングを明示的に設定（推奨：StandardCharsetsを使用）
        System.setProperty("file.encoding", StandardCharsets.UTF_8.name());

        // ロケールを日本語に設定
        Locale.setDefault(Locale.JAPAN);

        // SwingのUIをイベントディスパッチスレッドで実行
        SwingUtilities.invokeLater(() -> {
            try {
                // システムのデフォルトLook and Feelを使用
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // フォント関連の設定
                UIManager.put("swing.boldMetal", Boolean.FALSE);

                // 日本語関連のフォント設定をリファクタリング
                Font uiFont = new Font("SansSerif", Font.PLAIN, 12);
                setUIFont(new FontUIResource(uiFont));
                applySpecificFontSettings(uiFont);

            } catch (Exception e) {
                // Look and Feel適用エラーをログ出力
                logger.error("Look and Feelの適用に失敗しました", e);
            }

            // ToDoアプリケーションを作成して表示
            TodoApp app = new TodoApp();
            app.setVisible(true);

            logger.info("ToDoアプリケーションを起動しました。");
        });
    }

    /**
     * UIのフォントを一括で設定するユーティリティメソッド
     *
     * @param font 設定するフォント
     */
    private static void setUIFont(FontUIResource font) {
        UIManager.getDefaults().keySet().stream()
                .filter(key -> UIManager.get(key) instanceof FontUIResource)
                .forEach(key -> UIManager.put(key, font));
    }

    /**
     * 特定のコンポーネントにフォントを設定するメソッド
     * @param font 設定するフォント
     */
    private static void applySpecificFontSettings(Font font) {
        String[] keys = {
                "OptionPane.messageFont",
                "OptionPane.buttonFont",
                "TextField.font",
                "TextArea.font",
                "Label.font",
                "Button.font"
        };
        for (String key: keys) {
            UIManager.put(key, new FontUIResource(font));
        }
    }
}