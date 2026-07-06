package util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * Swing 日曆選擇欄位。
 * 使用者不用手動輸入日期，點「選取」即可從日曆挑選 yyyy-MM-dd。
 */
public class DatePickerField extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField textField;
    private JButton pickButton;

    public DatePickerField() {
        this(0, 0, 250, 38);
    }

    public DatePickerField(int x, int y, int width, int height) {
        setLayout(null);
        setOpaque(false);
        setBounds(x, y, width, height);

        int buttonWidth = 68;
        int gap = 6;

        textField = new JTextField();
        textField.setFont(ModernUI.NORMAL_FONT);
        textField.setEditable(false);
        textField.setBackground(ModernUI.CARD);
        textField.setForeground(ModernUI.BLACK);
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        textField.setBounds(0, 0, width - buttonWidth - gap, height);
        add(textField);

        pickButton = new JButton("選取");
        pickButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 12));
        pickButton.setFocusPainted(false);
        pickButton.setBackground(Color.WHITE);
        pickButton.setForeground(ModernUI.BLACK);
        pickButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        pickButton.setBounds(width - buttonWidth, 0, buttonWidth, height);
        add(pickButton);

        pickButton.addActionListener(e -> openCalendarDialog());
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text == null ? "" : text);
    }

    public Date getSqlDate(boolean required) {
        String value = getText();
        if (value == null || value.trim().isEmpty()) {
            if (required) {
                throw new IllegalArgumentException("請選擇日期");
            }
            return null;
        }
        return Date.valueOf(value.trim());
    }

    public Date getSqlDate() {
        return getSqlDate(false);
    }

    public void setSqlDate(Date date) {
        setText(date == null ? "" : date.toString());
    }

    public void clear() {
        setText("");
    }

    // 相容其他 class 可能使用的方法名稱
    public String getDate() {
        return getText();
    }

    public void setDate(String date) {
        setText(date);
    }

    public String getDateText() {
        return getText();
    }

    public void setDateText(String dateText) {
        setText(dateText);
    }

    public Date getSelectedDate() {
        return getSqlDate(false);
    }

    public void setSelectedDate(Date date) {
        setSqlDate(date);
    }

    public JTextField getDateField() {
        return textField;
    }

    private void openCalendarDialog() {
        JFrame frame = null;
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            frame = (JFrame) window;
        }

        JDialog dialog = new JDialog(frame, "選擇日期", true);
        dialog.setSize(430, 410);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(8, 8));
        dialog.getContentPane().setBackground(Color.WHITE);

        LocalDate initialDate = LocalDate.now();
        try {
            if (getText() != null && !getText().trim().isEmpty()) {
                initialDate = LocalDate.parse(getText().trim());
            }
        } catch (Exception ignored) {
            initialDate = LocalDate.now();
        }

        JComboBox<Integer> yearBox = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear - 30; y <= currentYear + 10; y++) {
            yearBox.addItem(y);
        }
        yearBox.setSelectedItem(initialDate.getYear());

        JComboBox<Integer> monthBox = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            monthBox.addItem(m);
        }
        monthBox.setSelectedItem(initialDate.getMonthValue());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(new JLabel("年份"));
        topPanel.add(yearBox);
        topPanel.add(new JLabel("月份"));
        topPanel.add(monthBox);
        dialog.add(topPanel, BorderLayout.NORTH);

        JPanel calendarPanel = new JPanel(new GridLayout(7, 7, 5, 5));
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        dialog.add(calendarPanel, BorderLayout.CENTER);

        Runnable refreshCalendar = () -> {
            calendarPanel.removeAll();

            String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
            for (String week : weeks) {
                JLabel label = new JLabel(week, SwingConstants.CENTER);
                label.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
                calendarPanel.add(label);
            }

            int year = (Integer) yearBox.getSelectedItem();
            int month = (Integer) monthBox.getSelectedItem();
            YearMonth ym = YearMonth.of(year, month);
            LocalDate firstDay = ym.atDay(1);

            int startOffset = firstDay.getDayOfWeek().getValue();
            if (startOffset == 7) {
                startOffset = 0;
            }

            for (int i = 0; i < startOffset; i++) {
                calendarPanel.add(createBlankDayButton());
            }

            for (int day = 1; day <= ym.lengthOfMonth(); day++) {
                final int selectedDay = day;
                LocalDate selectedDate = LocalDate.of(year, month, selectedDay);

                JButton dayButton = createDayButton(String.valueOf(day));

                if (selectedDate.equals(LocalDate.now())) {
                    dayButton.setBorder(new LineBorder(ModernUI.BLACK, 1, true));
                }

                dayButton.addActionListener(e -> {
                    setText(selectedDate.toString());
                    dialog.dispose();
                });

                calendarPanel.add(dayButton);
            }

            int usedCells = 7 + startOffset + ym.lengthOfMonth();
            for (int i = usedCells; i < 49; i++) {
                calendarPanel.add(createBlankDayButton());
            }

            calendarPanel.revalidate();
            calendarPanel.repaint();
        };

        yearBox.addActionListener(e -> refreshCalendar.run());
        monthBox.addActionListener(e -> refreshCalendar.run());
        refreshCalendar.run();

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        bottomPanel.setBackground(Color.WHITE);
        JButton clearButton = createBottomButton("清除");
        JButton todayButton = createBottomButton("今天");
        JButton cancelButton = createBottomButton("取消");
        bottomPanel.add(clearButton);
        bottomPanel.add(todayButton);
        bottomPanel.add(cancelButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        clearButton.addActionListener(e -> {
            setText("");
            dialog.dispose();
        });
        todayButton.addActionListener(e -> {
            setText(LocalDate.now().toString());
            dialog.dispose();
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private JButton createDayButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(ModernUI.BLACK);
        button.setPreferredSize(new Dimension(46, 32));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBorder(new LineBorder(new Color(180, 190, 200), 1, true));
        return button;
    }

    private JButton createBlankDayButton() {
        JButton button = createDayButton("");
        button.setEnabled(false);
        button.setBorder(null);
        button.setBackground(Color.WHITE);
        return button;
    }

    private JButton createBottomButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(ModernUI.BLACK);
        button.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        button.setPreferredSize(new Dimension(70, 30));
        return button;
    }
}
