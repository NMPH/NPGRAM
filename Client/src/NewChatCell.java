/**
 * Created by noyz on 6/23/17.
 */
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
public class NewChatCell extends ListCell<String> {
    HBox hbox = new HBox();
    Label chatTo = new Label();
    Pane pane = new Pane();
    Button makeChat = new Button();
    String lastItem;
    User myUser;
    User toChatUser;
    public NewChatCell(User myUser) {
        /*
        profileController is sent in order to change the followingsLabel when user accepts the request
         */
        super();
        this.myUser=myUser;
        hbox.getChildren().addAll(chatTo, pane, makeChat);
        HBox.setHgrow(pane, Priority.ALWAYS);
        makeChat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Chat newChat = new Chat(myUser.userFirstInfo.username,lastItem);
                myUser.chats.add(newChat);
                toChatUser.chats.add(newChat);
                Gettings.writeUser(toChatUser.userFirstInfo.username,toChatUser);
                Gettings.writeUser(myUser.userFirstInfo.username,myUser);
                try {
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setControllerFactory(c -> {
                        return new ChatController(newChat,primaryStage,myUser.userFirstInfo.username);
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

    private void initLabelAndButton(String chatToUsername){
        chatTo.setText(chatToUsername);
        makeChat.setText("make Chat");
    }

    @Override
    protected void updateItem(String chatToUsername, boolean empty) {
        super.updateItem(chatToUsername, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = chatToUsername;
            toChatUser = Gettings.getUser(chatToUsername);
            initLabelAndButton(chatToUsername);
            setGraphic(hbox);
        }
    }
}
