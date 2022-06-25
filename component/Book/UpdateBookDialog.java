package component.Book;

import utils.ScreenUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateBookDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;
    private String bkid;

    private JTextField idField;
    private JTextField nameField;
    private JTextField authorField;
    private JTextField pressField;
    private JTextField priceField;
    private JTextField stateField;
    String[][] tableData;


    public UpdateBookDialog(JFrame jf, String title, boolean isMedel, String bkid) {
        super(jf, title, isMedel);
        this.bkid = bkid;
//        this.tableData=tableData;
        //传参的时候使用二维数组的方式不可行，只能在Dialog当中获取当前tableData的数据--利用display的方法

        //组装视图
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox = Box.createVerticalBox();

        //组装图书编号
        Box idBox = Box.createHorizontalBox();
        JLabel idLabel = new JLabel("图书编号");
        idField = new JTextField(15);

        idBox.add(idLabel);
        idBox.add(Box.createHorizontalStrut(20));
        idBox.add(idField);

        //组装图书名称
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel("图书名称");
        nameField = new JTextField(15);

        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameField);

        //组装作者
        Box authorBox = Box.createHorizontalBox();
        JLabel authorLabel = new JLabel("图书作者");
        authorField = new JTextField(15);

        authorBox.add(authorLabel);
        authorBox.add(Box.createHorizontalStrut(20));
        authorBox.add(authorField);

        //组装出版社
        Box pressBox = Box.createHorizontalBox();
        JLabel pressLabel = new JLabel("图书出版社");
        pressField = new JTextField(15);

        pressBox.add(pressLabel);
        pressBox.add(Box.createHorizontalStrut(8));
        pressBox.add(pressField);

        //组装价格
        Box priceBox = Box.createHorizontalBox();
        JLabel priceLabel = new JLabel("图书价格");
        priceField = new JTextField(15);

        priceBox.add(priceLabel);
        priceBox.add(Box.createHorizontalStrut(20));
        priceBox.add(priceField);

        //组装状态
        Box stateBox = Box.createHorizontalBox();
        JLabel stateLabel = new JLabel("图书状态");
        stateField = new JTextField(15);

        stateBox.add(stateLabel);
        stateBox.add(Box.createHorizontalStrut(20));
        stateBox.add(stateField);

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton updateBtn = new JButton("修改");
        JButton quitBtn = new JButton("退出");

        btnBox.add(updateBtn);
        btnBox.add(Box.createHorizontalStrut(20));
        btnBox.add(quitBtn);

        displayData();

        //退出功能
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //修改行为,可以改价格和在馆状态
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String price = priceField.getText().trim();
                String state = stateField.getText().trim();

                if (price.equals("") || state.equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入完整数据! 请重新修改", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    Statement stmt;
                    Connection con = null;
                    try {
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");

                        String sql = "update book set bkPrice = '" + price + "',bkStatus='" + state + "' where bkID = '" + bkid + "'";
                        stmt = con.createStatement();
                        stmt.executeUpdate(sql);
                    } catch (SQLException a) {
                        System.out.println("" + a);
                    }
                    try {
                        con.close();
                    } catch (SQLException a) {
                        a.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "修改成功，请刷新");
                    dispose();//直接调用使得添加窗口消失
                }
            }
        });


        vBox.add(Box.createVerticalStrut(20));
        vBox.add(idBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(authorBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(pressBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(priceBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(stateBox);
        vBox.add(Box.createVerticalStrut(30));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(60));
        //为了使得两边有明显间隔
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);

    }
    //回显数据
    public void displayData() {
        Connection con = null;
        Statement sql;
        ResultSet rs;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
            sql = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = sql.executeQuery("SELECT*FROM book ");
            int recordAmount = 0, i = 0;
            rs.beforeFirst();
            while (rs.next()) {
                recordAmount++;
            }
            //获取tableData所有数据
            tableData = new String[recordAmount][6];
            rs.beforeFirst();
            while (rs.next()) {
                String bkID = rs.getString(1);
                tableData[i][0] = bkID;
                String bkName = rs.getString(2);
                tableData[i][1] = bkName;
                String bkAuthor = rs.getString(3);
                tableData[i][2] = bkAuthor;
                String bkPress = rs.getString(4);
                tableData[i][3] = bkPress;
                String bkPrice = rs.getString(5);
                tableData[i][4] = bkPrice;
                String bkStatus = rs.getString(6);
                tableData[i][5] = bkStatus;
                i++;
            }
            //从结果集tableData中取出选中的结果集合
            for (int j = 0; j < tableData.length; j++) {
                if (tableData[j][0].equals(bkid)) {
                    idField.setText(tableData[j][0]);
                    nameField.setText(tableData[j][1]);
                    authorField.setText(tableData[j][2]);
                    pressField.setText(tableData[j][3]);
                    stateField.setText(tableData[j][5]);
                    break;
                }
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
