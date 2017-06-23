import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import sun.misc.Cache;

/**
 * Created by noyz on 6/22/17.
 */
public class FollowRequestCell extends ListCell<String> {
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button accept = new Button("Accept");
    Button reject = new Button("Reject");
    String lastItem;
    User myUser;
    ProfileController profileController;
    public FollowRequestCell(User myUser,ProfileController profileController) {
        /*
        profileController is sent in order to change the followingsLabel when user accepts the request
         */
        super();
        this.profileController=profileController;
        this.myUser=myUser;
        hbox.getChildren().addAll(label, pane, accept,reject);
        HBox.setHgrow(pane, Priority.ALWAYS);
        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*
                tu user e khodesh boro followReqReceved esho bar dar bebar tu followers
                tu user e yaru boro az followReqSent bebar tu followings
                 */
                User toFollow = Gettings.getUser(lastItem);
                myUser.followRequestsRecieved.remove(toFollow.userFirstInfo.username);
                myUser.followersUsernames.add(toFollow.userFirstInfo.username);
                toFollow.followRequestsSent.remove(myUser.userFirstInfo.username);
                toFollow.followingsUsernames.add(myUser.userFirstInfo.username);
                Gettings.writeUser(myUser.userFirstInfo.username,myUser);
                Gettings.writeUser(toFollow.userFirstInfo.username,toFollow);
                hbox.setVisible(false);
                profileController.followersLabel.setText(new Integer(myUser.followersUsernames.size()).toString());
            }
        });
        reject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User toFollow = Gettings.getUser(lastItem);
                myUser.followRequestsRecieved.remove(toFollow.userFirstInfo.username);
                toFollow.followRequestsSent.remove(myUser.userFirstInfo.username);
                Gettings.writeUser(myUser.userFirstInfo.username,myUser);
                Gettings.writeUser(toFollow.userFirstInfo.username,toFollow);
                hbox.setVisible(false);
            }
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            label.setText(item != null ? item : "<null>");
            setGraphic(hbox);
        }
    }
}
