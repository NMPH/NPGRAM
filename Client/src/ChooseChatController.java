import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created by noyz on 6/23/17.
 */
public class ChooseChatController implements Initializable {
    Stage primaryStage;
    String myUsername;
    User myUser;
    ChooseChatController(String myUsername,Stage stage){
        this.myUsername=myUsername;
        myUser=Gettings.getUser(myUsername);
        this.primaryStage=stage;
    }
    @FXML
    Button addChatButton;
    @FXML
    StackPane chatListPane;
    public void addChat(){
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 300, 150);
        primaryStage.setScene(scene);
        ObservableList<String > list = FXCollections.observableArrayList();
        Iterator<String> followingUsernamesIterator = myUser.followingsUsernames.iterator();
        while (followingUsernamesIterator.hasNext()){
            String usernameToAddChat = followingUsernamesIterator.next();
            HashSet<String> alreadyChattingUsernames = myUser.getChattersSet();
            if(!alreadyChattingUsernames.contains(usernameToAddChat))
                list.add(usernameToAddChat);
        }
        ListView<String> lv = new ListView<>(list);
        lv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new NewChatCell(myUser);
            }
        });
        pane.getChildren().add(lv);
        primaryStage.show();
    }
    public void listChats(){
        HashSet<Chat> chats = myUser.chats;
        ObservableList<Chat> list = FXCollections.observableArrayList();
        Iterator<Chat> chatsIterator =chats.iterator();
        while(chatsIterator.hasNext()){
            list.add(chatsIterator.next());
        }
        ListView<Chat> lv = new ListView<>(list);
        lv.setCellFactory(new Callback<ListView<Chat>, ListCell<Chat>>() {
            @Override
            public ListCell<Chat> call(ListView<Chat> param) {
                return new ChatListCell(myUser);
            }
        });
        chatListPane.getChildren().add(lv);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("Application Closed by click to Close Button(X)");
                        Showings.showProfile(this,myUsername);
                    }
                });
            }
        });
        listChats();
    }
}
