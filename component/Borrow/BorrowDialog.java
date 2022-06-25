package component.Borrow;

import utils.ScreenUtils;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BorrowDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;

    private JTextField rdIDField;
    private JTextField bkIDField;

    private String[][] tableData;//二维结果集
    String []titles = {"读者编号","图书编号","借书日期","应还日期","实际还书日期"};

    Date date;
    SimpleDateFormat nowDate;



    public BorrowDialog(JFrame jf, String title, boolean isMedel) {
        super(jf, title, isMedel);
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox = Box.createVerticalBox();

        //组装读者编号（借书的人）
        Box rdIDBox = Box.createHorizontalBox();
        JLabel rdIDLabel = new JLabel("您的读者编号");
        rdIDField = new JTextField(15);

        rdIDBox.add(rdIDLabel);
        rdIDBox.add(Box.createHorizontalStrut(10));
        rdIDBox.add(rdIDField);

        //组装图书编号（被借的书）
        Box bkIDBox = Box.createHorizontalBox();
        JLabel bkIDLabel = new JLabel("您的借阅图书");
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

        //确认功能
        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rdID = rdIDField.getText().trim();
                String bkID = bkIDField.getText().trim();

                //若果两者都没输入，则提醒输入
                if (rdIDField.getText().trim().equals("") && bkIDField.getText().trim().equals("")) {
                    JLabel label = new JLabel("请输入读者编号和书号！");
                    label.setForeground(Color.red);
                    JOptionPane.showMessageDialog(rdIDField, label);
                }
                //若果没输入书号，则提醒输入
                else if (rdIDField.getText().trim().equals("")) {
                    JLabel label = new JLabel("请输入读者编号！");
                    label.setForeground(Color.red);
                    JOptionPane.showMessageDialog(rdIDField, label);
                }
                //若果没输入读者编号，则提醒输入
                else if (bkIDField.getText().trim().equals("")) {
                    JLabel label = new JLabel("请输入书号！");
                    label.setForeground(Color.red);
                    JOptionPane.showMessageDialog(bkIDField, label);
                }
                if (bkID.startsWith("rd") && rdID.startsWith("bk")) {
                    JLabel label = new JLabel("读者编号和书号输反了！");
                    label.setForeground(Color.red);
                    JOptionPane.showMessageDialog(rdIDField, label);
                }

                Connection con= null;
                Statement sql = null;
                String getcanLendQty, getrdType , getcanLendDay ;//分别从readertype表获得可借书数量、读者类别编号、可借书天数
                ResultSet rs1, rs2;

                String jilu1;
                String rdType = "";
                String DateBorrow = "";//借书日期
                String canLendQty = "";//能借书数量
                String canLendDay = "";//可借天数
                String DateLendPlan = "";//应还日期
                String DateLendAct = "0000-00-00 00:00:00";//默认实际还书日期
                try {
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
                    sql = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

                    //从reader表中用输入的rdID获取rdType
                    getrdType = "select rdType from reader where rdID='"+rdID+"'";
                    rs1 = sql.executeQuery(getrdType);

                    while (rs1.next()) {
                        rdType = rs1.getString(1);
                    }  // 为了获得reader表中的rdType,必须要添加rs.next()

                    //利用获取到的rdType从readerType中获取canLendQty
                    getcanLendQty = "select canLendQty from readerType where rdType='" + rdType + "'";
                    rs2 = sql.executeQuery(getcanLendQty);

                    while (rs2.next()) {
                        canLendQty = rs2.getString(1);
                    }//为了获得readerType表中的canLendQty

                    //利用获取到的rdType从readerType中获取canLendDay
                    getcanLendDay = "select canLendDay from readerType where rdType='" + rdType + "'";
                    rs2 = sql.executeQuery(getcanLendDay);

                    while (rs2.next()) {
                        canLendDay = rs2.getString(1);
                    }//为了获得readerType表中的canLendDay

                }catch (SQLException a) {
                    a.printStackTrace();
                }

                int bkIDCount = 0;     //定义一个计数器，看bkID在借书表中出现的次数从而判断是否在馆，是否能借
                int rdIDCount = 0;     //定义一个计数器，看rdID在借书表中出现的次数是否大于本人可借数，如果大了就不能再借
                int cannotRepeat = 0;    //定义一个计数器，防止重复借书，即重复输入
                requestData();

                for (int i = 0; i < tableData.length; i++) {
                    if (tableData[i][1].equals(bkID) && tableData[i][4].equals("0000-00-00 00:00:00"))
                        bkIDCount++;
                    if (tableData[i][0].equals(rdID) && tableData[i][4].equals("0000-00-00 00:00:00"))
                        rdIDCount++;
                    if (tableData[i][0].equals(rdID) && tableData[i][1].equals(bkID)&&tableData[i][4].equals("0000-00-00 00:00:00"))
                        cannotRepeat++;
                }

                //如果被借的书大于四本就表示该书不在馆
                try {
                    if (bkIDCount >= 4) {
                        JLabel label = new JLabel("该书不在馆！");
                        label.setForeground(Color.red);
                        JOptionPane.showMessageDialog(rdIDField, label);
                    } else if (rdIDCount >= Integer.parseInt(canLendQty)) {
                        JLabel label = new JLabel("您已超过可借书数量！");
                        label.setForeground(Color.red);
                        JOptionPane.showMessageDialog(rdIDField, label);
                    } else {
                        try {
                            if (cannotRepeat >= 1) {
                                JLabel label = new JLabel("您已借过该书！");
                                label.setForeground(Color.red);
                                JOptionPane.showMessageDialog(rdIDField, label);
                            } else {
                                DateLendPlan = gainTime(canLendDay);    //调用gainTime函数来获得应该归还书的日期
                                DateBorrow = nowDate.format(date);    //在gainTime函数体中可以得到当前借书的日期
                                jilu1 = "insert into borrow values('" + rdID + "','" + bkID + "','" + DateBorrow + "'," + "'" + DateLendPlan + "','" + DateLendAct + "')";
                                try {
                                    //创建一个触发事件，当有人借书后，borrow表里添加数据后，就触发这个事件：reader表里的borrowQty要加1（一次只能借一本书）
                                    String s = "create trigger t0 after insert"
                                            + " on borrow for each row"
                                            + " update reader set rdBorrowQty=rdBorrowQty+1"
                                            + " where rdID='" + rdID + "';";
                                    sql.executeUpdate(s);//执行触发器的语句
                                    sql.executeUpdate(jilu1);//调用该方法可进行插入、修改和更新

                                    sql.executeUpdate("drop trigger t0");   //创建完触发事件后要及时删除该触发器，不然会报错

                                    //判断借书之后书的数量是否大于4，如果大了就更新book表中的在馆状态
                                    int bkidCount = 0;
                                    for (int i = 0; i < tableData.length; i++) {
                                        if (tableData[i][1].equals(bkID))
                                            bkidCount++;
                                    }
                                    if (bkidCount >= 4) {
                                        String str = "update book set bkStatus='不在馆' where bkID='" + bkID + "'";
                                        sql.executeUpdate(str);
                                    }
                                    //创建一个添加成功窗口,当上面一条语句执行成功后，才能有添加成功窗口
                                    JOptionPane.showMessageDialog(null,"借书成功，请刷新");
                                    dispose();
                                } catch (SQLIntegrityConstraintViolationException ex) {
                                    System.out.println(jilu1);
                                }
                            }
                        } catch (SQLException c) {
                            c.printStackTrace();
                        }
                    }
                }catch (NumberFormatException a){
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
