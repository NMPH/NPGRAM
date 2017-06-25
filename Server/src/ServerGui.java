import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.ServerSocket;
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
    HashSet<User> userFiles;
    HashSet<UserFirstInfo> usersFirstInfo;
    ServerGui(HashSet<User> userFiles,  HashSet<UserFirstInfo> usersFirstInfo,Stage stage,StackPane onlinePeoplePane,
              StackPane allPeoplePane, StackPane offlinePeoplePane){
        this.stage=stage;
        this.onlinePeoplePane=onlinePeoplePane;
        this.allPeoplePane = allPeoplePane;
        this.offlinePeoplePane=offlinePeoplePane;
        this.userFiles=userFiles;
        this.usersFirstInfo=usersFirstInfo;
    }
    @Override
    protected Void call() throws Exception {
        while(!Thread.interrupted()) {
            ObservableList<String> onlineList = FXCollections.observableArrayList();
            ObservableList<String> offlineList = FXCollections.observableArrayList();
            ObservableList<String> allPeopleList = FXCollections.observableArrayList();
            Iterator<User> userIterator = userFiles.iterator();
            while (userIterator.hasNext()){
                User user = userIterator.next();
                allPeopleList.add(user.userFirstInfo.username);
                if(user.online){
                    onlineList.add(user.userFirstInfo.username);
                }else{
                    offlineList.add(user.userFirstInfo.username);
                }
            }
            ListView<String> onlv = new ListView<>(onlineList);
            onlv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new ServerUserCell();
                }
            });
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    // bar.setProgress(counter / 1000000.0);
                    onlinePeoplePane.getChildren().add(onlv);

                }
            });
            ListView<String> oflv = new ListView<>(offlineList);
            oflv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new ServerUserCell();
                }
            });
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    // bar.setProgress(counter / 1000000.0);
                    offlinePeoplePane.getChildren().add(oflv);

                }
            });
            ListView<String> allv = new ListView<>(allPeopleList);
            allv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new ServerUserCell();
                }
            });
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    // bar.setProgress(counter / 1000000.0);
                    allPeoplePane.getChildren().add(allv);

                }
            });

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