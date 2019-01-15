package com.sucre.gui;

import com.sucre.controller.BaiduController;
import com.sucre.utils.Printer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui implements Printer {
    private JButton start;
    private JComboBox mission;
    private JTextField IPcount;
    private JTextField filename;
    private JButton resume;
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

    private static Gui gui = new Gui();

    private Gui() {
        //设置mission选项的内容
        mission.setModel(new DefaultComboBoxModel(new String[]{"注册"}));

        /**
         * 加载id
         */
        loadid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaiduController.getInstance().loadId(filename.getText(), idtable, "loadid");
            }
        });
        /**
         * 加载vid
         */
        loadvid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaiduController.getInstance().loadVid(filename.getText(), vidtable);
            }
        });
        /**
         * /加入vid
         */
        addvid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaiduController.getInstance().addVid(filename.getText(), vidtable);
            }
        });
        /**
         * 加载cookie
         */
        loadcookie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaiduController.getInstance().loadCookie(filename.getText(), cookietable);
            }
        });

        /**
         * 开始任务
         */
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String m = (String) mission.getSelectedItem();
                int startcount = Integer.parseInt(startCount.getText());
                int threadnum = Integer.parseInt(threadNum.getText());
                BaiduController.getInstance().doMission(startcount, threadnum, false, m);
            }
        });
        /**
         * 暂停任务
         */
        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("暂停".equals(resume.getText())) {
                    resume.setText("继续");
                    BaiduController.getInstance().stop();
                } else {
                    BaiduController.getInstance().resume();
                    resume.setText("暂停");

                }
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

    /**
     * 实现打印的方法，让其它线程直接调用，显示返回结果。
     *
     * @param data
     */
    @Override
    public void print(String data) {
        // feedback.setText(data);
        feedback.setText(feedback.getText() + data + "\r\n");
    }

    /**
     * 取换ip的数量。
     *
     * @return
     */
    public int getIPcount() {
        return Integer.parseInt(IPcount.getText());
    }

    /**
     * 刷新列表数据。
     */
    public void refresh() {
        BaiduController.getInstance().refresh(cookietable);
    }

    /**
     * 取结束位置
     *
     * @return
     */
    public int getCounts() {
        return Integer.parseInt(endCount.getText());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
