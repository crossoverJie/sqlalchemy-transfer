package top.crossoverjie.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;
import top.crossoverjie.plugin.dialog.SampleDialogWrapper;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-02-29 00:38
 * @since JDK 1.8
 */
public class PopupDialogAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        System.out.println("actionPerformed..........");

        // Using the event, create and show a dialog
        Project currentProject = event.getProject();
        StringBuffer dlgMsg = new StringBuffer(event.getPresentation().getText() + " Selected!");
        String dlgTitle = event.getPresentation().getDescription();
        // If an element is selected in the editor, add info about it.
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if (nav != null) {
            dlgMsg.append(String.format("\nSelected Element: %s", nav.toString()));
        }
        Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());


        SampleDialogWrapper dialog = new SampleDialogWrapper();
        dialog.setSize(500, 500);
        if (dialog.showAndGet()) {
            System.out.println("ok");
            String text = dialog.getTextField().getText();
            System.out.println("text=" + text);
        }

    }

}
