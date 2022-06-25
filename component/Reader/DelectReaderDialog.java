package component.Reader;

import utils.ScreenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DelectReaderDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;
    String rdid;
    private JTextField idField;
    private JTextField typeField;
    private JTextField nameField;
    private JTextField deptField;
    private JTextField QQField;
    private JTextField rdBorrowQtyField;
    String[][] tableData;


    public DelectReaderDialog(JFrame jf, String title, boolean isMedel,String rdid) {
        super(jf, title, isMedel);
        this.rdid=rdid;

        //组装视图
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox = Box.createVerticalBox();

        //组装读者编号
        Box idBox = Box.createHorizontalBox();
        JLabel idLabel = new JLabel("读者编号");
        idField = new JTextField(15);

        idBox.add(idLabel);
        idBox.add(Box.createHorizontalStrut(20));
        idBox.add(idField);

        //组装读者类别号
        Box typeBox = Box.createHorizontalBox();
        JLabel typeLabel = new JLabel("读者类别号");
        typeField = new JTextField(15);

        typeBox.add(typeLabel);
        typeBox.add(Box.createHorizontalStrut(10));
        typeBox.add(typeField);

        //组装读者姓名
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel("读者姓名");
        nameField = new JTextField(15);

        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameField);

        //组装读者姓名
        Box deptBox = Box.createHorizontalBox();
        JLabel deptLabel = new JLabel("读者姓名");
        deptField = new JTextField(15);

        deptBox.add(deptLabel);
        deptBox.add(Box.createHorizontalStrut(20));
        deptBox.add(deptField);

        //组装读者QQ
        Box QQBox = Box.createHorizontalBox();
        JLabel QQLabel = new JLabel("QQ");
        QQField = new JTextField(15);

        QQBox.add(Box.createHorizontalStrut(20));
        QQBox.add(QQLabel);
        QQBox.add(Box.createHorizontalStrut(30));
        QQBox.add(QQField);

        //组装读者已借数量
        Box rdBorrowQtyBox = Box.createHorizontalBox();
        JLabel rdBorrowQtyLabel = new JLabel("已借数量");
        rdBorrowQtyField = new JTextField(15);

        rdBorrowQtyBox.add(rdBorrowQtyLabel);
        rdBorrowQtyBox.add(Box.createHorizontalStrut(20));
        rdBorrowQtyBox.add(rdBorrowQtyField);

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton yesBtn = new JButton("确认");
        JButton cancelBtn = new JButton("取消");
        btnBox.add(yesBtn);
        btnBox.add(Box.createHorizontalStrut(20));
        btnBox.add(cancelBtn);

        displayData();

        //取消功能
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        //确认删除功能
        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Statement stmt;
                Connection con = null;
                try {
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
                    String sql = "delete from reader where rdID='" + rdid + "'";
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
                JOptionPane.showMessageDialog(null, "删除成功，请刷新");
                dispose();//直接调用使得添加窗口消失
            }
        });

        //组装
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(idBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(typeBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(deptBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(QQBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(rdBorrowQtyBox);
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
    //回显数据
    public void displayData() {
        Connection con = null;
        Statement sql;
        ResultSet rs;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
            sql = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = sql.executeQuery("SELECT*FROM reader ");
            int recordAmount = 0, i = 0;
            rs.beforeFirst();
            while (rs.next()) {
                recordAmount++;
            }
            //获取tableData所有数据
            tableData = new String[recordAmount][6];
            rs.beforeFirst();
            while (rs.next()) {
                String rdID = rs.getString(1);
                tableData[i][0] = rdID;
                String rdType = rs.getString(2);
                tableData[i][1] = rdType;
                String rdName = rs.getString(3);
                tableData[i][2] = rdName;
                String rdDept = rs.getString(4);
                tableData[i][3] = rdDept;
                String rdQQ = rs.getString(5);
                tableData[i][4] = rdQQ;
                String rdBorrowQty = rs.getString(6);
                tableData[i][5] = rdBorrowQty;
                i++;
            }
            //从结果集tableData中取出选中的结果集合
            for (int j = 0; j < tableData.length; j++) {
                if (tableData[j][0].equals(rdid)) {
                    idField.setText(tableData[j][0]);
                    typeField.setText(tableData[j][1]);
                    nameField.setText(tableData[j][2]);
                    deptField.setText(tableData[j][3]);
                    QQField.setText(tableData[j][4]);
                    rdBorrowQtyField.setText(tableData[j][5]);
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
