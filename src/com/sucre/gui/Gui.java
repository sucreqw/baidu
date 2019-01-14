package com.sucre.gui;

import com.sucre.utils.Printer;

import javax.swing.*;

public class Gui  implements Printer {
    private JButton 开始Button;
    private JComboBox mission;
    private JTextField IPcount;
    private JTextField filename;
    private JButton 暂停Button;
    private JButton 导入Button;
    private JButton 导入cookieButton;
    private JButton 导入vidButton;
    private JButton 加入vidButton;
    private JTextField startCount;
    private JTextField endCount;
    private JTextField threadNum;
    private JTextArea feedback;
    private JPanel baidu;
    private JButton about;
    private JTable table1;
    private JTable table2;
    private JTable table3;

    private static Gui gui =  new Gui();

    private Gui() {

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Gui");
        frame.setContentPane(gui.baidu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(200, 100, 1024, 599);
    }

    public static Gui getInstance() {
        return gui;
    }

    @Override
    public void print(String data) {
        feedback.setText(data);
        feedback.setText(feedback.getText() + data + "\r\n");
    }
}
