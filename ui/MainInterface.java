package ui;

import utils.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class MainInterface {
    JFrame jf = new JFrame("图书馆");

    final int WIDTH = 500;
    final int HEIGHT = 300;


    //组装视图
    public void init() throws Exception {

        //设置窗口相关属性
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        //设置窗口居中
        jf.setResizable(false);

        //设置窗口内容
//        BackGroundPanel bgPanel = new BackGroundPanel(ImageIO.read(new File(PathUtils.getRealPath("library.jpg"))));
//文件路径自行修改
        BackGroundPanel bgPanel = new BackGroundPanel(ImageIO.read(new File("文件路径library.jpg")));
        //登录
        Box vBox = Box.createVerticalBox();

        //用户名
        Box uBox = Box.createHorizontalBox();
        JLabel uLabel = new JLabel("用户名:");
        JTextField uField = new JTextField(15);
        uField.setDocument(new IntDocument(9));//201902903  九位


        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(12));//水平间隔
        uBox.add(uField);

        //按钮
        Box btnBox = Box.createHorizontalBox();
        JButton loginBtn = new JButton("登录");
        JButton quitBtn = new JButton("退出");

        //登录功能
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int username = Integer.parseInt(uField.getText().trim());//学号只能为201902903这种格式
                        new MangerInterface(username);//将用户名传到管理菜单
                        jf.dispose();
                }catch (NumberFormatException a){}

            }
        });
        //退出功能
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //跳转到注册页面
                jf.dispose();
            }
        });

        btnBox.add(loginBtn);
        btnBox.add(Box.createHorizontalStrut(80));
        btnBox.add(quitBtn);
        //组装
        vBox.add(Box.createVerticalStrut(50));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(50));
        vBox.add(btnBox);

        bgPanel.add(vBox);
        jf.add(bgPanel);
        jf.setVisible(true);
    }


    //客户端入口
    public static void main(String[] args) {
        try {
            new MainInterface().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
