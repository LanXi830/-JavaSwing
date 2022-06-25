package ui;

//import component.Book.BookMangerComponent;
//import component.Borrow.BorrowMangerComponent;
//import component.Reader.ReaderMangerComponent;
//import component.ReaderType.ReaderTypeMangerComponent;
//import utils.ScreenUtils;

import utils.*;
import component.Book.*;
import component.Borrow.*;
import component.Reader.*;
import component.ReaderType.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MangerInterface {
    int username;

    final int WIDTH = 1000;
    final int HEIGHT = 600;
    public MangerInterface(int username){
        this.username=username;
        JFrame jf = new JFrame("用户 " +username+" ,欢迎您!");

        //设置窗口相关属性/.
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false);

        //设置菜单栏
        JMenuBar jmb = new JMenuBar();
        JMenu jMenu = new JMenu("设置");
        JMenuItem m1 = new JMenuItem("切换账号");
        JMenuItem m2 = new JMenuItem("退出程序");
        //退出程序功能
        m1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new MainInterface().init();
                    jf.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        //切换账号功能
        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jMenu.add(m1);
        jMenu.add(m2);
        jmb.add(jMenu);

        jf.setJMenuBar(jmb);
        //设置分割面板
        JSplitPane sp = new JSplitPane();
        sp.setContinuousLayout(true);//支持连续分布
        sp.setDividerLocation(150);
        sp.setDividerSize(7);
        //设置左侧内容
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("系统管理");
        DefaultMutableTreeNode ReaderTypeManger = new DefaultMutableTreeNode("读者类别管理");
        DefaultMutableTreeNode BookManger = new DefaultMutableTreeNode("图书管理");
        DefaultMutableTreeNode ReaderManger = new DefaultMutableTreeNode("读者管理");
        DefaultMutableTreeNode BorrowManger = new DefaultMutableTreeNode("借/还书");
        //组装树的叶子结点和根
        root.add(ReaderTypeManger);
        root.add(BookManger);
        root.add(ReaderManger);
        root.add(BorrowManger);

        //组装树
        Color color = new Color(200,220, 211);
        JTree tree = new JTree(root);
        //结点绘制器，使得左侧菜单获取头标
        MyRenderer myRenderer = new MyRenderer();
        myRenderer.setBackgroundNonSelectionColor(color);//未被选中时的颜色和背景颜色一样为color
        myRenderer.setBackgroundSelectionColor(new Color(140,140,140));//结点选中的颜色
        tree.setCellRenderer(myRenderer);

        tree.setBackground(color);
        //设置tree默认选中图书管理
        tree.setSelectionRow(2);
        //当条目选中后，这个方法会执行
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                //得到当前选中对象
                Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();//选中叶子结点

                if (ReaderTypeManger.equals(lastPathComponent)){//当选中的是读者类型管理
                    sp.setRightComponent(new ReaderTypeMangerComponent(jf));
                    sp.setDividerLocation(150);
                }else if (BookManger.equals(lastPathComponent)){//当选中的是图书管理
                    sp.setRightComponent(new BookMangerComponent(jf));
                    sp.setDividerLocation(150);
                }if (ReaderManger.equals(lastPathComponent)){//当选中的是读者管理
                    sp.setRightComponent(new ReaderMangerComponent(jf));
                    sp.setDividerLocation(150);
                }if (BorrowManger.equals(lastPathComponent)){//当选中的是借还管理
                    sp.setRightComponent(new BorrowMangerComponent(jf));
                    sp.setDividerLocation(150);
                }
            }
        });

        sp.setRightComponent(new BookMangerComponent(jf));//默认显示图书管理
        sp.setLeftComponent(tree);
        jf.add(sp);
        jf.setVisible(true);
    }
    //自定义结点绘制器
    private class MyRenderer extends DefaultTreeCellRenderer {
        private ImageIcon rootIcon = null;
        private ImageIcon ReaderTypeMangerIcon = null;
        private ImageIcon BookMangerIcon = null;
        private ImageIcon ReaderMangerIcon = null;
        private ImageIcon BorrowMangerIcon = null;

        private MyRenderer(){
//            rootIcon = new ImageIcon(PathUtils.getRealPath("systemManage.png"));
            rootIcon = new ImageIcon("文件路径+systemManage.png");
//            ReaderTypeMangerIcon = new ImageIcon(PathUtils.getRealPath("statisticsManage.png"));
            ReaderTypeMangerIcon = new ImageIcon("文件路径+statisticsManage.png");
//            BookMangerIcon = new ImageIcon(PathUtils.getRealPath("bookManage.png"));
            BookMangerIcon = new ImageIcon("文件路径+bookManage.png");
//            ReaderMangerIcon = new ImageIcon(PathUtils.getRealPath("userManage.png"));
            ReaderMangerIcon = new ImageIcon("文件路径+userManage.png");
//            BorrowMangerIcon = new ImageIcon(PathUtils.getRealPath("borrowManage.png"));
            BorrowMangerIcon = new ImageIcon("文件路径+borrowManage.png");
        }
        //当绘制树的每个结点都会调用该方法
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            //使用默认绘制
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            ImageIcon image = null;
            switch (row){
                case 0:
                    image = rootIcon;
                    break;
                case 1:
                    image = ReaderTypeMangerIcon;
                    break;
                case 2:
                    image = BookMangerIcon;
                    break;
                case 3:
                    image = ReaderMangerIcon;
                    break;
                case 4:
                    image = BorrowMangerIcon;
                    break;
            }
            this.setIcon(image);
            return this;
        }
    }
}
