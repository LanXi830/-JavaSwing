package component.Reader;

import utils.ScreenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AddReaderDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;


    public AddReaderDialog(JFrame jf, String title, boolean isMedel) {

        super(jf, title, isMedel);
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox = Box.createVerticalBox();

        //组装读者编号
        Box idBox = Box.createHorizontalBox();
        JLabel idLabel = new JLabel("读者编号");
        JTextField idField = new JTextField(15);

        idBox.add(idLabel);
        idBox.add(Box.createHorizontalStrut(20));
        idBox.add(idField);

        //组装读者编号
        Box typeBox = Box.createHorizontalBox();
        JLabel typeLabel = new JLabel("读者类别号");
        JTextField typeField = new JTextField(15);

        typeBox.add(typeLabel);
        typeBox.add(Box.createHorizontalStrut(10));
        typeBox.add(typeField);

        //组装读者名称
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel("读者姓名");
        JTextField nameField = new JTextField(15);

        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameField);

        //组装读者单位
        Box deptBox = Box.createHorizontalBox();
        JLabel deptLabel = new JLabel("读者单位");
        JTextField deptField = new JTextField(15);

        deptBox.add(deptLabel);
        deptBox.add(Box.createHorizontalStrut(20));
        deptBox.add(deptField);

        //组装读者QQ
        Box qqBox = Box.createHorizontalBox();
        JLabel qqLabel = new JLabel("QQ");
        JTextField qqField = new JTextField(15);

        qqBox.add(Box.createHorizontalStrut(20));
        qqBox.add(qqLabel);
        qqBox.add(Box.createHorizontalStrut(30));
        qqBox.add(qqField);

        //组装借书数量
        Box rdBorowQtyBox = Box.createHorizontalBox();
        JLabel rdBorowQtyLabel = new JLabel("已借数量");
        JTextField rdBorowQtyField = new JTextField(15);

        rdBorowQtyBox.add(rdBorowQtyLabel);
        rdBorowQtyBox.add(Box.createHorizontalStrut(20));
        rdBorowQtyBox.add(rdBorowQtyField);

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
                String id = idField.getText().trim();
                String type = typeField.getText().trim();
                String name = nameField.getText().trim();
                String dept = deptField.getText().trim();
                String qq = qqField.getText().trim();
                String rdBorrowQty = rdBorowQtyField.getText().trim();
                //判断输入数据是否正确
                if (id.equals("") || type.equals("") || name.equals("") || dept.equals("") || qq.equals("") || rdBorrowQty.equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入正确数据! 请重新添加", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    Statement stmt;
                    Connection con = null;
                    try {
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
                        String sql = "insert into reader values('" + id + "','" + type + "','" + name + "','" + dept + "','" + qq + "','" + rdBorrowQty + "')";
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
        vBox.add(idBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(typeBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(deptBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(qqBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(rdBorowQtyBox);
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
