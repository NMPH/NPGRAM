import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by asus on 6/25/2017.
 */
public class ServerGui extends Task<Void> {
    Stage stage;
    StackPane onlinePeoplePane;
    StackPane allPeoplePane;
    StackPane offlinePeoplePane;
    ServerGui(HashSet<User> userFiles,  HashSet<UserFirstInfo> usersFirstInfo,Stage stage,StackPane onlinePeoplePane,
              StackPane allPeoplePane, StackPane offlinePeoplePane){
        this.stage=stage;
        this.onlinePeoplePane=onlinePeoplePane;
        this.allPeoplePane = allPeoplePane;
        this.offlinePeoplePane=offlinePeoplePane;
    }
    @Override
    protected Void call() throws Exception {
        while(!Thread.interrupted()) {

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                System.out.println("INTRUPPTED EXCEPTION IN SHOWCHAT ChatController");
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }
}