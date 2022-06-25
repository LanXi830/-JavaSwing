package component.Borrow;

import component.Book.AddBookDialog;
import component.Book.UpdateBookDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BorrowMangerComponent extends Box {
    final int WIDTH = 850;
    final int HEIGHT = 600;
    JFrame jf = null;
    private JTable table;
    String []titles = {"读者编号","图书编号","借书日期","应还日期","实际还书日期"};
    private String[][] tableData;//二维结果集
    private DefaultTableModel tableModel;

    public BorrowMangerComponent(JFrame jf){
        //垂直布局
        super(BoxLayout.Y_AXIS);
        //组装视图
        this.jf = jf;

        JPanel btnPanel = new JPanel();
        Color color = new Color(203,220,217);
        btnPanel.setBackground(color);
        btnPanel.setMaximumSize(new Dimension(WIDTH,80));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));//使得默认流式布局从右开始

        JButton borrowBtn = new JButton("借书");
        JButton returnBtn = new JButton("还书");
        JButton refreshBtn = new JButton("刷新");

        //刷新功能
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestData();
                tableModel = new DefaultTableModel(tableData,titles);//重新组装表
                table.setModel(tableModel);
            }
        });
        //借书功能
        borrowBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //弹出一个窗口借书
                new BorrowDialog(jf,"欢迎使用借书系统：",true).setVisible(true);
            }
        });
        //还书功能
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹出一个窗口还书
                new ReturnDialog(jf,"欢迎使用还书系统",true).setVisible(true);
            }
        });


        btnPanel.add(borrowBtn);
        btnPanel.add(returnBtn);
        btnPanel.add(refreshBtn);
        this.add(btnPanel);

        //组装表格
        String []titles = {"读者编号","图书编号","借书日期","应还日期","实际还书日期"};

        //遍历表
        requestData();//只能获得结果集
        tableModel = new DefaultTableModel(tableData,titles);//重新组装表
        //设置table的性质
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };//不允许在表中编辑
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //只能选中一行

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
        this.setVisible(true);
    }
    //请求数据
    public void requestData(){

        Connection con = null;
        Statement sql;
        ResultSet rs;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
            sql = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = sql.executeQuery("SELECT*FROM borrow");
            int i = 0, recordAmount = 0;
            rs.beforeFirst();
            while (rs.next())
                recordAmount++;
            tableData = new String[recordAmount][5];
            rs.beforeFirst();
            while (rs.next()) {
                String rdID = rs.getString(1);
                tableData[i][0] = rdID;
                String bkID = rs.getString(2);
                tableData[i][1] = bkID;
                String DateBorrow = rs.getString(3);
                tableData[i][2] = DateBorrow;
                String DateLendPlan = rs.getString(4);
                tableData[i][3] = DateLendPlan;
                String DateLendAct = rs.getString(5);
                tableData[i][4] = DateLendAct;
                i++;
            }
        } catch (SQLException e) {
            System.out.println("" + e);
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
