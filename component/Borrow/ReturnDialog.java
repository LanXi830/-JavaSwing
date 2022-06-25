package component.Borrow;

import utils.ScreenUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReturnDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;

    private JTextField rdIDField;
    private JTextField bkIDField;

    private String[][] tableData;//二维结果集
    String []titles = {"读者编号","图书编号","借书日期","应还日期","实际还书日期"};

    Date date;
    SimpleDateFormat nowDate;

    public ReturnDialog(JFrame jf, String title, boolean isMedel){
        super(jf, title, isMedel);
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox = Box.createVerticalBox();

        //组装读者编号（还书的人）
        Box rdIDBox = Box.createHorizontalBox();
        JLabel rdIDLabel = new JLabel("您的读者编号");
        rdIDField = new JTextField(15);

        rdIDBox.add(rdIDLabel);
        rdIDBox.add(Box.createHorizontalStrut(10));
        rdIDBox.add(rdIDField);

        //组装图书编号（还的书）
        Box bkIDBox = Box.createHorizontalBox();
        JLabel bkIDLabel = new JLabel("您的归还图书");
        bkIDField = new JTextField(15);

        bkIDBox.add(bkIDLabel);
        bkIDBox.add(Box.createHorizontalStrut(10));
        bkIDBox.add(bkIDField);
        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton yesBtn = new JButton("确认");
        JButton cancelBtn = new JButton("取消");
        btnBox.add(yesBtn);
        btnBox.add(Box.createHorizontalStrut(30));
        btnBox.add(cancelBtn);

        //取消功能
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //确认还书功能
        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rdID = rdIDField.getText().trim();
                String bkID = bkIDField.getText().trim();

                SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Connection con = null;
                Statement sql = null;
                try {
                    if (bkID.startsWith("rd") && rdID.startsWith("bk")) {
                        JLabel label = new JLabel("读者编号和书号输反了！");
                        label.setForeground(Color.red);
                        JOptionPane.showMessageDialog(rdIDField, label);
                    } else if (bkID.startsWith("bk") && rdID.startsWith("rd")) {
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
                        sql = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

                        //创建一个触发事件，当有人还书后，即更新borrow表DateLendAct后，就触发这个事件：reader表里的borrowQty要减1(一次只能还一本书)
                        String str = "create trigger t1 after update"
                                + " on borrow for each row"
                                + " update reader set rdBorrowQty=rdBorrowQty-1"
                                + " where rdID= '" + rdID + "';";

                        sql.executeUpdate(str);
                        String s = "update borrow set DateLendAct=" + "'" + nowDate.format(new Date().getTime()) + "'"
                                + "where rdID='" + rdID + "'" + "and " + "bkID=" + "'" + bkID + "'" + "and " + "DateLendAct='0000-00-00 00:00:00'";

                        sql.executeUpdate(s);
                        sql.executeUpdate("drop trigger t1");   //创建完触发事件后要及时删除该触发器，不然会报错

                        requestData();

                        //还书之后要判断一下书的数目是否小于4，从而更新在馆状态
                        int bkidCount = 0;
                        for (int i = 0; i < tableData.length; i++) {
                            if (tableData[i][1].equals(bkID) && tableData[i][4].equals("0000-00-00 00:00:00"))
                                bkidCount++;
                        }
                        if (bkidCount <= 4) {
                            String string = "update book set bkStatus='在馆' where bkID='" + bkID + "'";
                            sql.executeUpdate(string);
                        }
                        //还书成功
                        JOptionPane.showMessageDialog(null, "还书成功，请刷新");
                        dispose();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }   try {
                    con.close();
                } catch (SQLException a) {
                    a.printStackTrace();
                }
            }
        });






        vBox.add(Box.createVerticalStrut(50));
        vBox.add(rdIDBox);
        vBox.add(Box.createVerticalStrut(60));
        vBox.add(bkIDBox);
        vBox.add(Box.createVerticalStrut(50));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(15));
        //为了使得两边有明显间隔
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
    }
//获取表中的数据
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


    //获取当前时间
    public String gainTime(String canLendDay) {
        int day = Integer.parseInt(canLendDay);
        date = new Date();
        nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return nowDate.format(date.getTime() + (long) day * 24 * 60 * 60 * 1000);
    }
}
