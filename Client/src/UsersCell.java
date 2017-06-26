/**
 * Created by noyz on 6/23/17.
 */
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sun.misc.Cache;

/**
 * Created by noyz on 6/22/17.
 */
public class UsersCell extends ListCell<String> {
    HBox vbox = new HBox();
    Label usernameLabel = new Label("(empty)");
    Pane pane = new Pane();
    Button openProfileButton = new Button("Show Profile");
    Button followButton = new Button("Show Profile");
    String lastItem;
    User myUser;
    Stage ProfileStage;
    public UsersCell(User myUser) {
        /*
        profileController is sent in order to change the followingsLabel when user accepts the request
         */
        super();
        this.myUser=myUser;
        vbox.getChildren().addAll(usernameLabel, pane, openProfileButton,followButton);
        HBox.setHgrow(pane, Priority.ALWAYS);
        openProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*
                tu user e khodesh boro followReqReceved esho bar dar bebar tu followers
                tu user e yaru boro az followReqSent bebar tu followings
                 */
                User peopleUser = Gettings.getUser(lastItem);
                Showings.showPeopleProfile(this,lastItem,myUser.userFirstInfo.username);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        });
        followButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User userPeople = Gettings.getUser(lastItem);
                if((myUser.followingsUsernames.contains(lastItem)||(myUser.followRequestsSent.contains(lastItem))))
                    return;
                userPeople.followRequestsRecieved.add(myUser.userFirstInfo.username);
                myUser.followRequestsSent.add(userPeople.userFirstInfo.username);
                Gettings.writeUser(userPeople.userFirstInfo.username,userPeople);
                Gettings.writeUser(myUser.userFirstInfo.username,myUser);
                followButton.setText("follow Request sent");
            }
        });
    }
private void initFollowButton(){
    if((myUser.followingsUsernames.contains(lastItem))){
        followButton.setText("following");
    }
    else if((myUser.followRequestsSent.contains(lastItem))){
        followButton.setText("follow Request sent");
    }
    else {
        followButton.setText("follow");
    }
}
private void setButtonsVisibilities(String item){
    if(myUser.followingsUsernames.contains(item)){
        openProfileButton.setVisible(true);
    }else{
        if(!lastItem.equals(myUser.userFirstInfo.username)){
            followButton.setVisible(true);
        }
    }
}
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        followButton.setVisible(false);
        openProfileButton.setVisible(false);
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            usernameLabel.setText(item != null ? item : "<null>");
            initFollowButton();
            setButtonsVisibilities(item );
            setGraphic(vbox);
        }
    }
}
