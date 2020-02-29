package top.crossoverjie.plugin.comsumer;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Consumer;

import javax.swing.*;
import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-02-29 04:40
 * @since JDK 1.8
 */
public class DirectorySelectedConsumer implements Consumer<List<VirtualFile>> {

    private JTextField inputFileTextField;

    public DirectorySelectedConsumer(JTextField inputFileTextField) {
        this.inputFileTextField = inputFileTextField;
    }

    @Override
    public void consume(List<VirtualFile> virtualFiles) {
        if (virtualFiles.size() == 1 ){
            String value = virtualFiles.get(0).getPath() ;
            inputFileTextField.setText(value);
        }
    }
}
