import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    Stage stage;
    EditProfileController(String username,Stage stage){
        this.username=username;
        user=Gettings.getUser(username);
        this.stage=stage;
    }
    String username;
    User user;
    @FXML
    Label changingFailure;
    /*@FXML
    Label exitEditProfileLabel;*/
    @FXML
    Label DoneLabel;
    @FXML
    TextField nameLabel,usernameLabel,bioLabel;
   /* public void exitEditProfileLabelclick(Event event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Showings.showProfile(this,username);
        stage.close();
    }*/
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
        try {
            Thread.sleep(10);
        }catch (InterruptedException e){
            System.out.println("in done label interrupted exception");
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        //Showings.showProfile(this,username);
        stage.close();
    }
    public void setFields(String username){
        usernameLabel.setText(username);
        nameLabel.setText(user.userFirstInfo.fullName);
        bioLabel.setText(user.bio);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("Application Closed by click to Close Button(X)");
                        Showings.showProfile(this,username);
                    }
                });
            }
        });
        setFields(username);
    }
}
