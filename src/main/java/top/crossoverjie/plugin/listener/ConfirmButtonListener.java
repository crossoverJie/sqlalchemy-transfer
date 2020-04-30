package top.crossoverjie.plugin.listener;

import com.intellij.openapi.ui.Messages;
import top.crossoverjie.plugin.core.DDLParse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
        String filePath = inputFileTextField.getText();
        String script = scriptTextArea.getText();

        System.out.println("path=" + filePath);
        System.out.println("script=" + script);

        try {
            String transfer = new DDLParse(script).transfer();

            Path path = Paths.get(filePath);
            Files.write(path, transfer.getBytes(), StandardOpenOption.APPEND);

        } catch (Exception e1) {
            System.err.println(e1);
            Messages.showMessageDialog("Generate failure, https://github.com/crossoverJie/sqlalchemy-transfer/issues/new", "Tips", Messages.getInformationIcon());
        }


        Messages.showMessageDialog(filePath + " has already finished", "Tips", Messages.getInformationIcon());
    }
}
