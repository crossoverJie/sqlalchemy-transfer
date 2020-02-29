package top.crossoverjie.plugin.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-02-29 03:56
 * @since JDK 1.8
 */
public class SampleDialogWrapper extends DialogWrapper {

    private EditorTextField textField;

    public SampleDialogWrapper() {
        // use current window as parent
        super(true);
        init();
        setTitle("Test DialogWrapper");

    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        textField = new EditorTextField();

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setSize(500, 500);

        JLabel label = new JLabel("testing");
        label.setPreferredSize(new Dimension(100, 100));
        dialogPanel.add(label, BorderLayout.CENTER);


        textField.setPlaceholder("# 请填入 DDL 语句！");
        textField.setSize(300, 300);
        textField.setOneLineMode(true);
        dialogPanel.add(textField);

        JButton button = new JButton("选择文件") ;
        dialogPanel.add(button) ;

//        FileChooserDescriptor descriptor = new FileChooserDescriptor(true,false,false,false,false,false);
//        Project project = DummyProject.getInstance() ;
//        File file = new File("/");
//        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
//        FileChooser.chooseFiles(descriptor, project, virtualFile, new DirectorySelectedConsumer());

        return dialogPanel;
    }


    public EditorTextField getTextField() {
        return textField;
    }
}