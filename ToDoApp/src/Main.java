import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.Font;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;
import java.util.Locale;

/**
 * ToDoアプリケーションのメインクラス
 */
public class Main {
    /**
     * アプリケーションのエントリーポイント
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        // 文字エンコーディングを明示的に設定
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        // ロケールを日本語に設定
        Locale.setDefault(new Locale("ja", "JP"));
        
        // SwingのUIをイベントディスパッチスレッドで実行
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // システムのデフォルトLook and Feelを使用
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    
                    // フォント関連の設定
                    UIManager.put("swing.boldMetal", Boolean.FALSE);
                    
                    // 基本的なフォントを設定
                    setUIFont(new FontUIResource("SansSerif", Font.PLAIN, 12));
                    
                    // 日本語関連の設定
                    UIManager.put("OptionPane.messageFont", new FontUIResource("SansSerif", Font.PLAIN, 12));
                    UIManager.put("OptionPane.buttonFont", new FontUIResource("SansSerif", Font.PLAIN, 12));
                    UIManager.put("TextField.font", new FontUIResource("SansSerif", Font.PLAIN, 12));
                    UIManager.put("TextArea.font", new FontUIResource("SansSerif", Font.PLAIN, 12));
                    UIManager.put("Label.font", new FontUIResource("SansSerif", Font.PLAIN, 12));
                    UIManager.put("Button.font", new FontUIResource("SansSerif", Font.PLAIN, 12));
                } catch (Exception e) {
                    // Nimbusが利用できない場合はデフォルトのLook and Feelを使用
                    System.err.println("Nimbusルック&フィールを適用できませんでした: " + e.getMessage());
                }
                
                // ToDoアプリケーションを作成して表示
                TodoApp app = new TodoApp();
                app.setVisible(true);
                
                System.out.println("ToDoアプリケーションを起動しました。");
            }
        });
    }
    
    /**
     * UIのフォントを一括で設定するユーティリティメソッド
     *
     * @param font 設定するフォント
     */
    private static void setUIFont(FontUIResource font) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}