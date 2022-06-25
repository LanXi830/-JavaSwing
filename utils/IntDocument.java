package utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

/**
 * # -*- coding:utf-8 -*- #
 * 作者:lanxi
 * 日期:2021年11月12日21时35分
 *该方法用于限制输入只能为数字 自行使用
 */
public class IntDocument extends PlainDocument {
    private static final long serialVersionUID = 1L;
    private String reg = "^[0-9]";

    public IntDocument(int Size){
        reg = reg +"[0-9]"+ "{0," +(Size-1) +"}$";
    }

    @Override
    public void insertString(int offset, String s, AttributeSet attributeSet)
            throws BadLocationException {

        if (offset == 0 && s.equals(".")) {

            Toolkit.getDefaultToolkit().beep();
            return;
        }
        String str = this.getText(0, this.getLength()) + s;
        int i = this.getText(0, this.getLength()).indexOf(".");
        if (i == -1 && str.endsWith(".")) {
            super.insertString(offset, s, attributeSet);
            return;
        }
        if (str.matches(reg)) {
            super.insertString(offset, s, attributeSet);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
