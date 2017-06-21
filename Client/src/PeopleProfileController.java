import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    User user;
    @FXML
    ImageView profilePicture;
    @FXML
    Label bioLabel;
    public PeopleProfileController(String username){
        user = Gettings.getUser(username);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image profileImage=null;
        try {
            if (user.profilePicture != null) {
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(user.profilePicture));
                profileImage = new Image(imageInputStream);
            }
        }catch (IOException e){
            System.out.println("problem in reading the user profile picture in PeopleProfileController Initializer()");
            e.printStackTrace();
        }
        if(profileImage!=null) {
            profilePicture.setImage(profileImage);
        }
        this.bioLabel.setText(user.bio);
    }
}
