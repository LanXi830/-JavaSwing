package component.Book;


import utils.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AddBookDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;


    public AddBookDialog(JFrame jf, String title, boolean isMedel) {
        super(jf, title, isMedel);


        //组装视图
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox = Box.createVerticalBox();

        //组装图书编号
        Box idBox = Box.createHorizontalBox();
        JLabel idLabel = new JLabel("图书编号");
        JTextField idField = new JTextField(15);

        idBox.add(idLabel);
        idBox.add(Box.createHorizontalStrut(20));
        idBox.add(idField);

        //组装图书名称
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel("图书名称");
        JTextField nameField = new JTextField(15);

        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameField);

        //组装作者
        Box authorBox = Box.createHorizontalBox();
        JLabel authorLabel = new JLabel("图书作者");
        JTextField authorField = new JTextField(15);

        authorBox.add(authorLabel);
        authorBox.add(Box.createHorizontalStrut(20));
        authorBox.add(authorField);

        //组装出版社
        Box pressBox = Box.createHorizontalBox();
        JLabel pressLabel = new JLabel("图书出版社");
        JTextField pressField = new JTextField(15);

        pressBox.add(pressLabel);
        pressBox.add(Box.createHorizontalStrut(8));
        pressBox.add(pressField);

        //组装价格
        Box priceBox = Box.createHorizontalBox();
        JLabel priceLabel = new JLabel("图书价格");
        JTextField priceField = new JTextField(15);

        priceBox.add(priceLabel);
        priceBox.add(Box.createHorizontalStrut(20));
        priceBox.add(priceField);

        //组装状态
        Box stateBox = Box.createHorizontalBox();
        JLabel stateLabel = new JLabel("图书状态");
        JTextField stateField = new JTextField(15);

        stateBox.add(stateLabel);
        stateBox.add(Box.createHorizontalStrut(20));
        stateBox.add(stateField);

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
                String name = nameField.getText().trim();
                String author = authorField.getText().trim();
                String press = pressField.getText().trim();
                String price = priceField.getText().trim();
                String state = stateField.getText().trim();
                //添加数据库
                if (id.equals("") || name.equals("") || author.equals("") || press.equals("") || price.equals("") || state.equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入正确数据! 请重新添加", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    Statement stmt;
                    Connection con = null;
                    try {
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
                        String sql = "insert into book values('" + id + "','" + name + "','" + author + "','" + press + "','" + price + "','" + state + "')";
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

//                addWindowListener(new WindowAdapter() {
//                    @Override
//                    public void windowClosed(WindowEvent e) {
//                        System.out.println(2);
//                    }
//                });//dispose的监听是Closed，而❌号事件的监听是Closing
            }
        });


        vBox.add(Box.createVerticalStrut(20));
        vBox.add(idBox);
        vBox.add(Box.createVerticalStrut(10))  ;
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(authorBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(pressBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(priceBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(stateBox);
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
