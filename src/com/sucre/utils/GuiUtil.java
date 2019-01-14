package com.sucre.utils;

import com.sucre.entity.Baidu;
import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GuiUtil {

	public static void loadTable(JTable table, MutiList list) {
		// String[] temp = //list.get(0).split("\\|");
		// Weibo weibo=(Weibo) list.gets(0);

		String[] columnName = { "id", "pass", "uid", "cookie", "s", "name" };

		// for (int i = 0; i < columnName.length; i++) {
		// columnName[i] = "列数" + i;
		// }

		String[][] data = new String[list.getSize()][6];
		MyUtil.print(String.valueOf(list.getSize()), Factor.getGui());
		for (int i = 0; i < list.getSize(); i++) {
			// temp = list.get(i).split("\\|");

			// for (int j = 0; j < temp.length; j++) {
			// data[i][j] = temp[j];
			// }
			Baidu Baidu = (Baidu) list.gets(i);
			data[i][0] = Baidu.getId();
			data[i][1] = Baidu.getPass();
			data[i][2] = Baidu.getUid();
			data[i][3] = Baidu.getCookie();
			data[i][4] = Baidu.getS();
			data[i][5] = Baidu.getName();

		}
		DefaultTableModel d = new DefaultTableModel(data, columnName);
		table.setModel(d);
	}

	public static void loadTableVid(JTable table, MutiList list) {
		if(list.getSize()>50000){return;}
		String[] temp = list.get(0).split("\\|");
		String[] columnName = new String[temp.length];

		for (int i = 0; i < columnName.length; i++) {
			columnName[i] = "列数" + i;
		}

		String[][] data = new String[list.getSize()][temp.length];
		MyUtil.print(String.valueOf(list.getSize()), Factor.getGui());
		for (int i = 0; i < list.getSize(); i++) {
			temp = list.get(i).split("\\|");

			for (int j = 0; j < temp.length; j++) {
				data[i][j] = temp[j];
			}

		}
		DefaultTableModel d = new DefaultTableModel(data, columnName);
		table.setModel(d);
	}

}
