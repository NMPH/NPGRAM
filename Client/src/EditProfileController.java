import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by noyz on 6/20/17.
 */
public class EditProfileController implements Initializable{
    EditProfileController(String username){
        this.username=username;
        user=Gettings.getUser(username);
    }
    String username;
    User user;
    @FXML
    Label changingFailure;
    @FXML
    Label exitEditProfileLabel;
    @FXML
    Label DoneLabel;
    @FXML
    TextField nameLabel,usernameLabel,bioLabel;
    public void exitEditProfileLabelclick(Event event){
        Showings.showProfile(this,username);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void DoneLabelClick(Event event){
        String name = nameLabel.getText();
        String oldUsername = user.userFirstInfo.username;
        String username = usernameLabel.getText();
        String bio = bioLabel.getText();
        Boolean isValid=false;
        if(Validations.isValidFullName(name)){
            if(!username.equals(oldUsername)){
                if(Validations.isValidUserName(username)){
                    isValid=true;
                }
            } else isValid=true;
        }
        if (!isValid) {
            changingFailure.setText("Please review the changed information");
            return;
        }
        user.bio=bio;
        user.userFirstInfo.username=username;
        user.userFirstInfo.fullName=name;
        Gettings.writeUser(oldUsername,user);
        Showings.showProfile(this,username);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void setFields(String username){
        usernameLabel.setText(username);
        nameLabel.setText(user.userFirstInfo.fullName);
        bioLabel.setText(user.bio);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setFields(username);
    }
}
