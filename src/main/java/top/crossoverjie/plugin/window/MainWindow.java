package top.crossoverjie.plugin.window;

import com.intellij.openapi.wm.ToolWindow;
import top.crossoverjie.plugin.listener.ChooseFileButtonListener;
import top.crossoverjie.plugin.listener.ConfirmButtonListener;

import javax.swing.*;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-02-29 21:45
 * @since JDK 1.8
 */
public class MainWindow {
    private JPanel windowContent;
    private JTextArea scriptTextArea;
    private JTextField inputFileTextField;
    private JButton chooseFileButton;
    private JButton confirmButton;
    private JCheckBox decimalCheckBox;


    public MainWindow(ToolWindow toolWindow) {

        chooseFileButton.addActionListener(new ChooseFileButtonListener(inputFileTextField));
        confirmButton.addActionListener(new ConfirmButtonListener(inputFileTextField, scriptTextArea, decimalCheckBox));
    }

    public JPanel getContent() {
        return windowContent;
    }
}
