package logbook.plugin.overageship.gui;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import logbook.internal.LoggerHolder;
import logbook.internal.gui.Tools;
import logbook.internal.gui.WindowController;
import logbook.plugin.PluginContainer;
import logbook.plugin.gui.MainCommandMenu;

public class OverageShipListMenu implements MainCommandMenu {

    @Override
    public MenuItem getContent() {
        MenuItem menu = new MenuItem("改造可能な艦娘");
        menu.setOnAction(e -> {
            try {
                Stage stage = new Stage();
                URL url = OverageShipListMenu.class.getClassLoader()
                        .getResource("overageship/gui/overage_ship_list.fxml");
                FXMLLoader loader = new FXMLLoader(url);
                loader.setClassLoader(PluginContainer.getInstance().getClassLoader());
                Parent root = loader.load();
                stage.setScene(new Scene(root));
                WindowController controller = loader.getController();
                controller.setWindow(stage);

                stage.initOwner(menu.getParentPopup().getOwnerWindow());
                stage.setTitle("改造可能な艦娘");
                Tools.Windows.setIcon(stage);
                Tools.Windows.defaultCloseAction(controller);
                Tools.Windows.defaultOpenAction(controller);
                stage.show();
            } catch (Exception ex) {
                LoggerHolder.get().warn("改造可能な艦娘を開けませんでした", ex);
            }
        });
        return menu;
    }

}