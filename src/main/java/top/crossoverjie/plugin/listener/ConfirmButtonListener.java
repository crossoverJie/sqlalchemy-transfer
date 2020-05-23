package top.crossoverjie.plugin.listener;

import com.intellij.openapi.ui.Messages;
import top.crossoverjie.plugin.core.DDLParse;
import top.crossoverjie.plugin.core.ThreadLocalHolder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
    private JCheckBox decimalCheckBox;

    public ConfirmButtonListener(JTextField inputFileTextField, JTextArea scriptTextArea, JCheckBox decimalCheckBox) {
        this.inputFileTextField = inputFileTextField;
        this.scriptTextArea = scriptTextArea;
        this.decimalCheckBox = decimalCheckBox;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String filePath = inputFileTextField.getText();
        String script = scriptTextArea.getText();

        System.out.println("path=" + filePath);
        System.out.println("script=" + script);
        boolean selected = decimalCheckBox.isSelected();
        System.out.println("checked=" + selected);
        if (selected){
            Messages.showMessageDialog("decimal will be transform to (db.DECIMAL(15, 2)", "Tips", Messages.getInformationIcon());
        }
        ThreadLocalHolder.setDecimal(selected);

        try {
            String transfer = new DDLParse(script).transfer();

            Path path = Paths.get(filePath);
            Files.write(path, transfer.getBytes(), StandardOpenOption.APPEND);

        } catch (Exception e) {
            File file = new File(filePath);
            PrintStream ps = null;
            try {
                ps = new PrintStream(file);
            } catch (FileNotFoundException e1) {
                System.err.println(e1);
            }
            e.printStackTrace(ps);
            System.err.println(e);
            Messages.showMessageDialog("Generate failure, https://github.com/crossoverJie/sqlalchemy-transfer/issues/new", "Tips", Messages.getInformationIcon());


        }finally {
            ThreadLocalHolder.removeDecimal();
        }


        Messages.showMessageDialog(filePath + " has already finished", "Tips", Messages.getInformationIcon());
    }
}
