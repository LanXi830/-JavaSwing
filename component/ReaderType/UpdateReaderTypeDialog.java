package component.ReaderType;

import utils.ScreenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateReaderTypeDialog extends JDialog {
    final int WIDTH =400;
    final int HEIGHT =300;
    private String rdType;


    private JTextField typeField;
    private JTextField nameField;
    private JTextField canLendQtyField;
    private JTextField canLendDayField;
    String[][]tableData;



    public UpdateReaderTypeDialog(JFrame jf, String title, boolean isMedel,String rdType ){
        super(jf,title,isMedel);
        this.rdType=rdType;
//        this.tableData=tableData;
        //传参的时候使用二维数组的方式不可行，只能在Dialog当中获取当前tableData的数据--利用display的方法

        //组装视图
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox =Box.createVerticalBox();

        //组装读者类别号
        Box typeBox = Box.createHorizontalBox();
        JLabel typeLabel = new JLabel("读者类别号");
        typeField = new JTextField(15);

        typeBox.add(typeLabel);
        typeBox.add(Box.createHorizontalStrut(20));
        typeBox.add(typeField);

        //组装读者类别名称
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel("读者类别名称");
        nameField = new JTextField(15);

        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(8));
        nameBox.add(nameField);

        //组装可借书数量
        Box canLendQtyBox = Box.createHorizontalBox();
        JLabel canLendQtyLabel = new JLabel("可借书数量");
        canLendQtyField = new JTextField(15);

        canLendQtyBox.add(canLendQtyLabel);
        canLendQtyBox.add(Box.createHorizontalStrut(20));
        canLendQtyBox.add(canLendQtyField);

        //组装可借书天数
        Box canLendDayBox = Box.createHorizontalBox();
        JLabel canLendDayLabel = new JLabel("可借书天数");
        canLendDayField = new JTextField(15);

        canLendDayBox.add(canLendDayLabel);
        canLendDayBox.add(Box.createHorizontalStrut(20));
        canLendDayBox.add(canLendDayField);



        //组装按钮
        Box btnBox = Box.createHorizontalBox();

        JButton updateBtn = new JButton("修改");


        JButton quitBtn = new JButton("退出");

        btnBox.add(updateBtn);
        btnBox.add(Box.createHorizontalStrut(20));
        btnBox.add(quitBtn);
        //修改功能
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取修改的数据
                String name= nameField.getText().trim();
                String canLendQty = canLendQtyField.getText().trim();
                String canLendDay = canLendDayField.getText().trim();


                if (canLendQty.equals("")||canLendDay.equals("")){
                    JOptionPane.showMessageDialog(null, "请输入完整数据! 请重新修改", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    Statement stmt;
                    Connection con = null;
                    try {
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");

                        String sql = "update readertype set rdTypeName='"+name+"', canLendQty='"+canLendQty+"',canLendDay='"+canLendDay+"'where rdType = '"+rdType+"'";
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
                    JOptionPane.showMessageDialog(null,"修改成功，请刷新");
                    dispose();//直接调用使得添加窗口消失
                }
            }
        });
        //退出功能
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        displayData();





        vBox.add(Box.createVerticalStrut(20));
        vBox.add(typeBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(canLendQtyBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(canLendDayBox);
        vBox.add(Box.createVerticalStrut(30));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(60));
        //为了使得两边有明显间隔
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);

    }//回显数据
    public void displayData(){
        Connection con = null;
        Statement sql;
        ResultSet rs;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
            sql = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs=sql.executeQuery("SELECT*FROM readertype ");
            int recordAmount = 0,i=0;
            rs.beforeFirst();
            while (rs.next()){
                recordAmount++;
            }
            //获取tableData所有数据
            tableData = new String[recordAmount][4];
            rs.beforeFirst();
            while (rs.next()) {
                String rdType = rs.getString(1);
                tableData[i][0] = rdType;
                String rdName = rs.getString(2);
                tableData[i][1] = rdName;
                String canLendQty = rs.getString(3);
                tableData[i][2] = canLendQty;
                String canLendDay = rs.getString(4);
                tableData[i][3] = canLendDay;
                i++;
            }
            //从结果集tableData中取出选中的结果集合
            for (int j =0;j<tableData.length;j++){
                if (tableData[j][0].equals(rdType)){
                    typeField.setText(tableData[j][0]);
                    nameField.setText(tableData[j][1]);
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
