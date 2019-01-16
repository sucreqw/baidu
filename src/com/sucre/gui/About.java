package com.sucre.gui;

import javax.swing.*;

public class About {
    private JPanel About;
    private JTextArea aboutTxt;

    public static void main(String[] args) {
        JFrame frame = new JFrame("About");
        frame.setContentPane(new About().About);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setBounds(300, 100, 624, 699);
        frame.setVisible(true);

    }
}
