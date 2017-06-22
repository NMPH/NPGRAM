import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by noyz on 6/21/17.
 */
public class PeopleProfileController implements Initializable{
    User userPeople;
    User myUser;
    @FXML
    ImageView profilePicture;
    @FXML
    Label bioLabel;
    @FXML
    Button followButton,unFollowButton;
    @FXML
    Label followersLabel,followingsLabel;
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
        userPeople.followRequestsRecieved.remove(myUser.userFirstInfo.username);
        Gettings.writeUser(userPeople.userFirstInfo.username,userPeople);
        Gettings.writeUser(myUser.userFirstInfo.username,myUser);
        unFollowButton.setVisible(false);
        followButton.setVisible(true);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        followButton.setVisible(false);
        unFollowButton.setVisible(false);
        if((myUser.isFollowRequestSent(userPeople.userFirstInfo.username))||(myUser.isFollowing(userPeople.userFirstInfo.username))){
            unFollowButton.setVisible(true);
        }else{
            followButton.setVisible(true);
        }
        Image profileImage=null;
        try {
            if (userPeople.profilePicture != null) {
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(userPeople.profilePicture));
                profileImage = new Image(imageInputStream);
            }
        }catch (IOException e){
            System.out.println("problem in reading the user profile picture in PeopleProfileController Initializer()");
            e.printStackTrace();
        }
        if(profileImage!=null) {
            profilePicture.setImage(profileImage);
        }
        this.bioLabel.setText(userPeople.bio);
        followersLabel.setText(new Integer(userPeople.followersUsernames.size()).toString());
        followingsLabel.setText(new Integer(userPeople.followingsUsernames.size()).toString());
    }
}
