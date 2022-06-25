package component.ReaderType;

import component.Book.DelectBookDialog;
import component.Reader.AddReaderDialog;
import component.Reader.UpdateReaderDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ReaderTypeMangerComponent extends Box {
    final int WIDTH = 850;
    final int HEIGHT = 600;
    JFrame jf = null;
    private JTable table;
    String[] titles = {"读者类别号","读者类别名称","可借书数量","可借书天数"};

    private String[][] tableData;//二维结果集
    private DefaultTableModel tableModel;

    public ReaderTypeMangerComponent(JFrame jf) {
        //垂直布局
        super(BoxLayout.Y_AXIS);
        //组装视图
        this.jf = jf;

        JPanel btnPanel = new JPanel();
        Color color = new Color(203, 220, 217);
        btnPanel.setBackground(color);
        btnPanel.setMaximumSize(new Dimension(WIDTH, 80));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));//使得默认流式布局从右开始

        JButton addBtn = new JButton("添加读者类型");
        JButton updateBtn = new JButton("修改读者类型");
        JButton deleteBtn = new JButton("删除读者类型");
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
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹出一个对话框,让用户输入信息
                new AddReaderTypeDialog(jf, "添加读者类型", true).setVisible(true);
            }
        });
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取当前选中的rdType
                int selectedRow = table.getSelectedRow();//如果有选择的条目，则返回行号,没有选择返回-1
                if (selectedRow==-1){
                    JOptionPane.showMessageDialog(jf,"请选择要修改的行！");
                    return;
                }
                String rdType = tableModel.getValueAt(selectedRow, 0).toString();
                new UpdateReaderTypeDialog(jf,"修改读者类型",true,rdType).setVisible(true);

            }
        });

        //删除功能
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取选中的条目
                int selectedRow = table.getSelectedRow();//如果有选择的条目，则返回行号,没有选择返回-1
                if (selectedRow==-1){
                    JOptionPane.showMessageDialog(jf,"请选择要删除的行！");
                    return;
                }

                //删除提示！
                int result= JOptionPane.showConfirmDialog(jf,"请您确认删除选中的条目吗？","确认删除",JOptionPane.YES_NO_OPTION);
                if (result!=JOptionPane.YES_OPTION){
                    return;
                }
                String rdtype = tableModel.getValueAt(selectedRow, 0).toString();
                new DelectReaderTypeDialog(jf,"删除读者类型",true,rdtype).setVisible(true);
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);
        this.add(btnPanel);
        //组装表格
        String[] titles = {"读者类别号","读者类别名称","可借书数量","可借书天数"};


        //遍历表
        requestData();//只能获得结果集
        tableModel = new DefaultTableModel(tableData, titles);//重新组装表
        //设置table的性质
        table = new JTable(tableModel) {
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
    public void requestData() {

        Connection con = null;
        Statement sql;
        ResultSet rs;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/booksdb", "root", "genius816");
            sql = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = sql.executeQuery("SELECT*FROM readertype");
            int i = 0, recordAmount = 0;
            rs.beforeFirst();
            while (rs.next())
                recordAmount++;
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
