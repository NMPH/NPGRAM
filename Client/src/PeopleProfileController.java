import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created by noyz on 6/21/17.
 */
public class PeopleProfileController implements Initializable{
    @FXML
    StackPane postsPane;
    User userPeople;
    User myUser;
    final Circle clip = new Circle(75, 45, 45);
    @FXML
    ImageView profilePicture;
    @FXML
    Label bioLabel, postLabel;
    @FXML
    Button followButton,unFollowButton;
    @FXML
    Label followersLabel,followingsLabel;
    @FXML
    Button blockButton;
    @FXML
    Button unblockButton;
    public void block(ActionEvent event){
        myUser.blockedUsernames.add(userPeople.userFirstInfo.username);
        userPeople.blockedByUsernames.add(myUser.userFirstInfo.username);
        myUser.followersUsernames.remove(userPeople.userFirstInfo.username);
        userPeople.followingsUsernames.remove(myUser.userFirstInfo.username);
        Gettings.writeUser(userPeople.userFirstInfo.username,userPeople);
        Gettings.writeUser(myUser.userFirstInfo.username,myUser);
        blockButton.setVisible(false);
        unblockButton.setVisible(true);
    }
    public void unblock(Event event){
        myUser.blockedUsernames.remove(userPeople.userFirstInfo.username);
        userPeople.blockedByUsernames.remove(myUser.userFirstInfo.username);
        Gettings.writeUser(userPeople.userFirstInfo.username,userPeople);
        Gettings.writeUser(myUser.userFirstInfo.username,myUser);
        blockButton.setVisible(true);
        unblockButton.setVisible(false);
    }
    public void showPeoplePosts(){
        ObservableList<Post> list = FXCollections.observableArrayList();
        Iterator<Post> postIterator = Gettings.getUser(userPeople.userFirstInfo.username).posts.iterator();
        while (postIterator.hasNext()){
            list.add(postIterator.next());
        }
        ListView<Post> lv = new ListView<>(list);
        lv.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            @Override
            public ListCell<Post> call(ListView<Post> param) {
                return new HomeCell(myUser);
            }
        });
        postsPane.getChildren().add(lv);
    }
    public PeopleProfileController(String userPeopleUsername, String myUsername){
        userPeople = Gettings.getUser(userPeopleUsername);
        myUser=Gettings.getUser(myUsername);
    }
    public void followButtonClicked(ActionEvent event){
            userPeople.followRequestsRecieved.add(myUser.userFirstInfo.username);
            myUser.followRequestsSent.add(userPeople.userFirstInfo.username);
            Gettings.writeUser(userPeople.userFirstInfo.username,userPeople);
            Gettings.writeUser(myUser.userFirstInfo.username,myUser);
            followButton.setVisible(false);
            unFollowButton.setVisible(true);
    }
    public void unFollowButtonClicked(ActionEvent event){
        myUser.followRequestsSent.remove(userPeople.userFirstInfo.username);
        myUser.followingsUsernames.remove(userPeople.userFirstInfo.username);
        userPeople.followersUsernames.remove(myUser.userFirstInfo.username);
        userPeople.followRequestsRecieved.remove(myUser.userFirstInfo.username);
        Gettings.writeUser(userPeople.userFirstInfo.username,userPeople);
        Gettings.writeUser(myUser.userFirstInfo.username,myUser);
        followersLabel.setText((new Integer(userPeople.followersUsernames.size()).toString()));
        unFollowButton.setVisible(false);
        followButton.setVisible(true);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        postsPane.setVisible(false);
        if(myUser.followingsUsernames.contains(userPeople.userFirstInfo.username)){
            postsPane.setVisible(true);
            showPeoplePosts();
        }
        blockButton.setVisible(false);
        unblockButton.setVisible(false);
        followButton.setVisible(false);
        unFollowButton.setVisible(false);
        if((myUser.isFollowRequestSent(userPeople.userFirstInfo.username))||(myUser.isFollowing(userPeople.userFirstInfo.username))){
            unFollowButton.setVisible(true);
        }else{
            followButton.setVisible(true);
        }
        if(myUser.blockedUsernames.contains(userPeople.userFirstInfo.username)){
            unblockButton.setVisible(true);
        }
        else if((!myUser.isFollowing(userPeople.userFirstInfo.username))&&(!myUser.isFollowRequestSent(userPeople.userFirstInfo.username))){
            blockButton.setVisible(true);
        }
        BufferedImage profileImage=null;
        if (userPeople.profilePicture != null) {
            //BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(user.profilePicture));
            profileImage =  ImageFunctions.ByteArrayToBufferedImage(userPeople.profilePicture);;
        }
            if (userPeople.profilePicture != null) {
                Image card = SwingFXUtils.toFXImage(profileImage, null );
                profilePicture.setImage(card);
                profilePicture.setClip(clip);
            }
        this.bioLabel.setText(userPeople.bio);
        followersLabel.setText(new Integer(userPeople.followersUsernames.size()).toString());
        followingsLabel.setText(new Integer(userPeople.followingsUsernames.size()).toString());
            postLabel.setText((new Integer(userPeople.posts.size())).toString());
    }
}
