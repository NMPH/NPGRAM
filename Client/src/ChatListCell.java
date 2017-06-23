/**
 * Created by noyz on 6/23/17.
 */
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sun.misc.Cache;

import java.io.IOException;

/**
 * Created by noyz on 6/22/17.
 */
public class ChatListCell extends ListCell<Chat> {
    String chatToUsername;
    HBox hbox = new HBox();
    Label chatTo = new Label();
    Pane pane = new Pane();
    Button openChat = new Button();
    Chat lastItem;
    User myUser;
    ProfileController profileController;
    public ChatListCell(User myUser) {

        /*
        profileController is sent in order to change the followingsLabel when user accepts the request
         */
        super();
        this.myUser=myUser;
        hbox.getChildren().addAll(chatTo, pane, openChat);
        HBox.setHgrow(pane, Priority.ALWAYS);
        openChat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setControllerFactory(c -> {
                        return new ChatController(lastItem,primaryStage,myUser.userFirstInfo.username);
                    });
                    Parent root = loader.load(getClass().getResource("Chat.fxml").openStream());
                    ChatController chatController = (ChatController) loader.getController();
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {
                    System.out.println("Problem in showChooseChats");
                    e.printStackTrace();
                }
                //open chat xml file which contains chatCell
            }
        });
    }

    private void initLabelAndButton(Chat chat){
        if(myUser.userFirstInfo.username.equals(chat.chatter1)){
            chatToUsername=chat.chatter2;
        }else if(myUser.userFirstInfo.username.equals(chat.chatter2)){
            chatToUsername=chat.chatter1;
        }else{
            System.out.println("BIG PROBLEM NON OF the chatters is me!!!");
        }
        chatTo.setText(chatToUsername);
        openChat.setText("openChat");
    }

    @Override
    protected void updateItem(Chat item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            initLabelAndButton(item);
            //label.setText(item != null ? item : "<null>");
            setGraphic(hbox);
        }
    }
}
