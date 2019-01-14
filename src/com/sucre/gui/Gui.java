package com.sucre.gui;

import com.sucre.controller.Controller;
import com.sucre.utils.Printer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui  implements Printer {
    private JButton 开始Button;
    private JComboBox mission;
    private JTextField IPcount;
    private JTextField filename;
    private JButton 暂停Button;
    private JButton loadid;
    private JButton loadcookie;
    private JButton loadvid;
    private JButton addvid;
    private JTextField startCount;
    private JTextField endCount;
    private JTextField threadNum;
    private JTextArea feedback;
    private JPanel baidu;
    private JButton about;
    private JTable idtable;
    private JTable vidtable;
    private JTable cookietable;
    private JScrollPane idscroll;
    private JScrollPane vidscroll;
    private JScrollPane cookiescroll;

    private static Gui gui =  new Gui();

    private Gui() {

        loadid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
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

    /**
     * 取换ip的数量。
     * @return
     */
    public int getIPcount() {
        return Integer.parseInt(IPcount.getText());
    }
    /**
     * 刷新列表数据。
     */
    public void refresh() {
        Controller.getInstance().refresh(cookietable);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
