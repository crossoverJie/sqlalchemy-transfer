package top.crossoverjie.plugin.listener;

import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-02-29 23:09
 * @since JDK 1.8
 */
public class ConfirmButtonListener implements ActionListener {

    private JTextField inputFileTextField;
    private JTextArea scriptTextArea;

    public ConfirmButtonListener(JTextField inputFileTextField, JTextArea scriptTextArea) {
        this.inputFileTextField = inputFileTextField;
        this.scriptTextArea = scriptTextArea;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String path = inputFileTextField.getText();
        String script = scriptTextArea.getText();

        System.out.println("path=" + path);
        System.out.println("script=" + script);

        try {
//            List<TableElement> tables = new DDLParse(script).generateDDLInfo();
//
//
//
//            for (TableElement table : tables) {
//                System.out.println(table.getTableName());
//            }
        } catch (Exception e1) {
            System.err.println(e1);
        }


        Messages.showMessageDialog(path + "has already finished", "notify", Messages.getInformationIcon());
    }
}
