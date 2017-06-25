import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * Created by asus on 6/25/2017.
 */
public class ServerGUIPageController implements Initializable {
    HashSet<User> userFiles;
    HashSet<UserFirstInfo> userFirstInfos;
    Stage stage;
        ServerGUIPageController(HashSet<User> userFiles, HashSet<UserFirstInfo> usersFirstInfo, Stage stage){
        this.userFiles = userFiles;
        this.userFirstInfos=usersFirstInfo;
        this.stage = stage;
    }
    @FXML
    StackPane onlinePeoplePane;
    @FXML
    StackPane allPeoplePane;
    @FXML
    StackPane offlinePeoplePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServerGui serverGuiThread = new ServerGui(userFiles,userFirstInfos,stage,onlinePeoplePane,allPeoplePane,offlinePeoplePane);
        Thread t = new Thread(serverGuiThread);
        t.start();
        Stage stage = new Stage();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("Application Closed by click to Close Button(X)");
                        t.interrupt();
                    }
                });
            }
        });
    }
}
