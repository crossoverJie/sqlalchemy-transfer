package top.crossoverjie.plugin.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020-02-29 21:44
 * @since JDK 1.8
 */
public class MainWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MainWindow mainWindow = new MainWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mainWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
