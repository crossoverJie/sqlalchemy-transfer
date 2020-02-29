package top.crossoverjie.plugin.listener;

import com.intellij.openapi.command.impl.DummyProject;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import top.crossoverjie.plugin.comsumer.DirectorySelectedConsumer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-02-29 23:09
 * @since JDK 1.8
 */
public class ChooseFileButtonListener implements ActionListener {

    private JTextField inputFileTextField ;

    public ChooseFileButtonListener(JTextField inputFileTextField) {
        this.inputFileTextField = inputFileTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        Project project = DummyProject.getInstance();
        File file = new File("/");
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
        FileChooser.chooseFiles(descriptor, project, virtualFile, new DirectorySelectedConsumer(inputFileTextField));
    }
}
