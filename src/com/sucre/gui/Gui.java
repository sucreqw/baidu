package com.sucre.gui;


import com.sucre.controller.Controller;
import com.sucre.utils.MyUtil;
import com.sucre.utils.Printer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Gui implements Printer {
    private JButton start;
    private JComboBox mission;
    private JTextField IPcount;
    public JTextField filename;
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
    private JLabel image;
    //存放验证码输入状态，等待service线程来取，1表示换一张，2表示输入完毕。
    private int status;

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
                Controller.getInstance().loadId(filename.getText(), idtable, "loadid");
            }
        });
        /**
         * 加载vid
         */
        loadvid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().loadVid(filename.getText(), vidtable);
            }
        });
        /**
         * /加入vid
         */
        addvid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().addVid(filename.getText(), vidtable);
            }
        });
        /**
         * 加载cookie
         */
        loadcookie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().loadCookie(filename.getText(), cookietable);
            }
        });

        /**
         * 开始任务
         */
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status =0;
                String m = (String) mission.getSelectedItem();
                int startcount = Integer.parseInt(startCount.getText());
                int threadnum = Integer.parseInt(threadNum.getText());
                Controller.getInstance().doMission(startcount, threadnum, false, m);
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
                    Controller.getInstance().stop();
                } else {
                    Controller.getInstance().resume();
                    resume.setText("暂停");

                }
            }
        });
        /**
         * 关于窗体。
         */
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                About.main(null);

            }
        });
        //文本框按钮点击事件。
        filename.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                //super.keyTyped(e);
                //filename.setText(String.valueOf(e.getExtendedKeyCode()));
                int event=e.getExtendedKeyCode();
                switch (event){

                    case 10:
                        //表示输入完毕
                        status=2;
                        break;

                    case 192:
                        //表示换一张
                        status=1;
                        filename.setText("");
                        break;
                     default:
                         //filename.setText(String.valueOf(event));
                         break;
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
        Controller.getInstance().refresh(cookietable);
    }

    /**
     * 取结束位置
     *
     * @return
     */
    public int getCounts() {
        return Integer.parseInt(endCount.getText());
    }

    /**
     * 把验证码显示到窗体上。
     * @param pic 图片的byte
     */
    public void showPic(byte [] pic){
        //使用字节数组，实例化ImageIcon对象
        Icon icon = new ImageIcon(pic);
        //建立画布对象。
        Graphics g =image.getGraphics() ;
        //把照片流画到画布G上，imageIcon.getImage：取到图片对象，x,y为图像要显示 在窗体上的位置，whidth,height为图片的宽和高，最后是窗体的panel
        g.drawImage(((ImageIcon) icon).getImage(), 5, 5,icon.getIconWidth(), icon.getIconHeight(), baidu) ;
        //画到lable上
        image.paint(g) ;
    }


    /**
     * //存放验证码输入状态，等待service线程来取
     * @return 1表示换一张，2表示输入完毕。
     */
    public int getStatus(){
        int ret=status;
        //状态初始化，避免重复取值。
        status=0;
        return ret;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }


}
