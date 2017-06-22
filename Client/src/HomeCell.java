/**
 * Created by noyz on 6/22/17.
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class HomeCell extends ListCell<Post> {
    VBox hbox = new VBox();
    Label caption = new Label("(empty)");
    Label owner = new Label("(empty");
    Pane pane = new Pane();
    //Button accept = new Button("Accept");
    //Button reject = new Button("Reject");
    ImageView imageView = new ImageView();
    Post lastItem;
    User myUser;
    public HomeCell(User myUser) {
        super();
        this.myUser=myUser;
        hbox.getChildren().addAll(imageView, pane, caption,owner);
        HBox.setHgrow(pane, Priority.ALWAYS);
        /*accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                *//*
                tu user e khodesh boro followReqReceved esho bar dar bebar tu followers
                tu user e yaru boro az followReqSent bebar tu followings
                 *//*
                User toFollow = Gettings.getUser(lastItem);
                myUser.followRequestsRecieved.remove(toFollow.userFirstInfo.username);
                myUser.followersUsernames.add(toFollow.userFirstInfo.username);
                toFollow.followRequestsSent.remove(myUser.userFirstInfo.username);
                toFollow.followingsUsernames.add(myUser.userFirstInfo.username);
                Gettings.writeUser(myUser.userFirstInfo.username,myUser);
                Gettings.writeUser(toFollow.userFirstInfo.username,toFollow);
                hbox.setVisible(false);
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
        });*/
    }

    @Override
    protected void updateItem(Post item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            //label.setText(item != null ? item : "<null>");
            caption.setText(lastItem.caption);
            owner.setText(lastItem.ownerUsername);
            if(lastItem.image!=null) {
                try {
                    BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(lastItem.image));
                    Image image = new Image(imageInputStream);
                    imageView.setImage(image);
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(40);
                } catch (IOException e) {
                    System.out.println("ERROR while reading from image");
                    e.printStackTrace();
                }
            }
            System.out.println(lastItem.image);

            setGraphic(hbox);
        }
    }
}
