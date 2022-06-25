package component.ReaderType;

import utils.IntDocument;
import utils.ScreenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AddReaderTypeDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;


    public AddReaderTypeDialog(JFrame jf, String title, boolean isMedel) {

        super(jf, title, isMedel);
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox = Box.createVerticalBox();

        //组装读者类别号
        Box typeBox = Box.createHorizontalBox();
        JLabel typeLabel = new JLabel("读者类别号");
        JTextField typeField = new JTextField(15);

        typeBox.add(typeLabel);
        typeBox.add(Box.createHorizontalStrut(20));
        typeBox.add(typeField);

        //组装读者类别名
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel("读者类别名");
        JTextField nameField = new JTextField(15);

        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameField);

        //组装可借天数
        Box canLendDayBox = Box.createHorizontalBox();
        JLabel canLendDayLabel = new JLabel("可借天数");
        JTextField canLendDayField = new JTextField(15);
        canLendDayField.setDocument(new IntDocument(3));//可借天数不能超过三位数

        canLendDayBox.add(canLendDayLabel);
        canLendDayBox.add(Box.createHorizontalStrut(30));
        canLendDayBox.add(canLendDayField);

        //组装可借书数量
        Box canLendQtyBox = Box.createHorizontalBox();
        JLabel canLendQtyLabel = new JLabel("可借书数量");
        JTextField canLendQtyField = new JTextField(15);
        canLendQtyField.setDocument(new IntDocument(2));
        //可借数量不能超过两位数 因为每本书只有四本

        canLendQtyBox.add(canLendQtyLabel);
        canLendQtyBox.add(Box.createHorizontalStrut(20));
        canLendQtyBox.add(canLendQtyField);


        //组装按钮

        Box btnBox = Box.createHorizontalBox();
        JButton addBtn = new JButton("添加");
        JButton cancelBtn = new JButton("取消");
        btnBox.add(addBtn);
        btnBox.add(Box.createHorizontalStrut(20));
        btnBox.add(cancelBtn);

        //取消功能
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        //添加功能
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户的录入
                String type = typeField.getText().trim();
                String name = nameField.getText().trim();
                String canLendDay = canLendDayField.getText().trim();
                String canLendQty = canLendQtyField.getText().trim();
                //判断输入数据是否正确
                if (type.equals("") || name.equals("") || canLendDay.equals("") || canLendQty.equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入正确数据! 请重新添加", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    Statement stmt;
                    Connection con = null;
                    try {
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
                        String sql = "insert into readertype values('" + type + "','" + name + "','" + canLendDay + "','" + canLendQty + "')";
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
                }
                JOptionPane.showMessageDialog(null, "添加成功，请刷新");
                dispose();//直接调用使得添加窗口消失
            }
        });



        vBox.add(Box.createVerticalStrut(20));
        vBox.add(typeBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(canLendDayBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(canLendQtyBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(20));
        //为了使得两边有明显间隔
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
    }
}
