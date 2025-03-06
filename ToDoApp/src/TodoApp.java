import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ToDoアプリケーションのメインGUIクラス
 */
public class TodoApp extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "todo_data.ser";
    private static final Logger logger = LoggerFactory.getLogger(TodoApp.class);

    private List<TodoItem> todoItems;
    private DefaultListModel<TodoItem> listModel;
    private JList<TodoItem> todoList;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JButton addButton;
    private JButton deleteButton;
    private JButton toggleButton;
    private JLabel statusLabel;
    private JLabel createdAtLabel;
    private JLabel completedAtLabel;

    /**
     * コンストラクタ
     */
    public TodoApp() {
        todoItems = new ArrayList<>();
        loadData();

        setTitle("ToDo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // メインパネルの設定
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 左側のパネル（ToDoリスト）
        JPanel leftPanel = createLeftPanel();

        // 右側のパネル（詳細と入力フォーム）
        JPanel rightPanel = createRightPanel();

        // 分割パネルの作成
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftPanel, rightPanel);
        splitPane.setDividerLocation(350);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // ステータスバー
        statusLabel = new JLabel("ToDoアイテム: " + todoItems.size());
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // 終了時にデータを保存
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveData)); // メソッド参照を利用
    }

    /**
     * 左側のパネル（ToDoリスト）を作成
     */
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        panel.setBorder(BorderFactory.createTitledBorder(null, "ToDoリスト",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                titleFont));

        // リストモデルとJListの作成
        listModel = new DefaultListModel<>();
        for (TodoItem item : todoItems) {
            listModel.addElement(item);
        }

        todoList = new JList<>(listModel);
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoList.setCellRenderer(new TodoListCellRenderer());
        todoList.addListSelectionListener(e -> { // ラムダ式を使用
            if (!e.getValueIsAdjusting()) {
                updateDetailView();
            }
        });

        JScrollPane scrollPane = new JScrollPane(todoList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ボタンパネル
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        deleteButton = new JButton("削除");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteTodoItem()); // ラムダ式を使用

        toggleButton = new JButton("完了/未完了");
        toggleButton.setEnabled(false);
        toggleButton.addActionListener(e -> toggleTodoItemStatus()); // ラムダ式を使用

        buttonPanel.add(deleteButton);
        buttonPanel.add(toggleButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 右側のパネル（詳細と入力フォーム）を作成
     */
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 12);

        // 詳細表示パネル
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(BorderFactory.createTitledBorder(null, "詳細情報",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                titleFont));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel createdLabel = new JLabel("作成日時:");
        createdLabel.setFont(labelFont);
        detailPanel.add(createdLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        createdAtLabel = new JLabel("");
        createdAtLabel.setFont(labelFont);
        detailPanel.add(createdAtLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel completedLabel = new JLabel("完了日時:");
        completedLabel.setFont(labelFont);
        detailPanel.add(completedLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        completedAtLabel = new JLabel("");
        completedAtLabel.setFont(labelFont);
        detailPanel.add(completedAtLabel, gbc);

        // 入力フォームパネル
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(null, "新規ToDo",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                titleFont));

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("タイトル:");
        titleLabel.setFont(labelFont);
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField(20);
        titleField.setFont(labelFont);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel descLabel = new JLabel("詳細:");
        descLabel.setFont(labelFont);
        formPanel.add(descLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(labelFont);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descScrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        addButton = new JButton("追加");
        addButton.setFont(labelFont);
        addButton.addActionListener(e -> addTodoItem()); // ラムダ式を使用
        formPanel.add(addButton, gbc);

        // 右パネルに詳細パネルと入力フォームパネルを追加
        panel.add(detailPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 選択されたToDoアイテムの詳細表示を更新
     */
    private void updateDetailView() {
        TodoItem selectedItem = todoList.getSelectedValue();
        if (selectedItem != null) {
            createdAtLabel.setText(selectedItem.getFormattedCreatedAt());
            completedAtLabel.setText(selectedItem.getFormattedCompletedAt());
            deleteButton.setEnabled(true);
            toggleButton.setEnabled(true);
        } else {
            createdAtLabel.setText("");
            completedAtLabel.setText("");
            deleteButton.setEnabled(false);
            toggleButton.setEnabled(false);
        }
    }

    /**
     * 新しいToDoアイテムを追加
     */
    private void addTodoItem() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "タイトルを入力してください。",
                    "入力エラー",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        TodoItem newItem = new TodoItem(title, description);
        todoItems.add(newItem);
        listModel.addElement(newItem);

        // 入力フィールドをクリア
        titleField.setText("");
        descriptionArea.setText("");

        // ステータスを更新
        updateStatus();
    }

    /**
     * 選択されたToDoアイテムを削除
     */
    private void deleteTodoItem() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            TodoItem item = listModel.getElementAt(selectedIndex);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "「" + item.getTitle() + "」を削除しますか？",
                    "削除の確認",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                todoItems.remove(item);
                listModel.remove(selectedIndex);
                updateDetailView();
                updateStatus();
            }
        }
    }

    /**
     * 選択されたToDoアイテムの完了/未完了ステータスを切り替え
     */
    private void toggleTodoItemStatus() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            TodoItem item = listModel.getElementAt(selectedIndex);
            item.setCompleted(!item.isCompleted());

            // リストを更新
            todoList.repaint();

            // 詳細表示を更新
            updateDetailView();
        }
    }

    /**
     * ステータスバーを更新
     */
    private void updateStatus() {
        int total = todoItems.size();
        long completed = todoItems.stream().filter(TodoItem::isCompleted).count();
        statusLabel.setText(String.format("ToDoアイテム: %d (完了: %d, 未完了: %d)",
                total, completed, total - completed));
        // データを保存
        saveData();
    }

    /**
     * ToDoデータをファイルに保存
     */
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(Paths.get(DATA_FILE))))) {
            oos.writeObject(todoItems);
            logger.info("データを保存しました。");
        } catch (IOException e) {
            logger.error("データの保存に失敗しました", e);

            // エラーが発生した場合はダイアログを表示
            JOptionPane.showMessageDialog(this,
                    "データの保存に失敗しました: " + e.getMessage(),
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ToDoデータをファイルから読み込み
     */
    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new BufferedInputStream(Files.newInputStream(file.toPath())))) {
                todoItems = (List<TodoItem>) ois.readObject();
                logger.info(todoItems.size() + "件のToDoデータを読み込みました。");
            } catch (IOException | ClassNotFoundException e) {
                logger.error("データの読み込みに失敗しました", e);

                JOptionPane.showMessageDialog(this,
                        "データの読み込みに失敗しました: " + e.getMessage(),
                        "エラー",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /**
     * Todoリスト描画するクラス
     */
    private static class TodoListCellRenderer extends JLabel implements ListCellRenderer<TodoItem> {
        public TodoListCellRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getListCellRendererComponent(JList<? extends TodoItem> list, TodoItem value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
}