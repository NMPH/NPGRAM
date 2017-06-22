import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created by noyz on 6/21/17.
 */
public class SearchController implements Initializable{
    SearchController(String myUsername){
        this.myUsername=myUsername;
    }
    String myUsername;
    @FXML
    Label userNotFoundLabel;
    @FXML
    Button searchButton;
    @FXML
    TextField usernameToSearchTextField;
    public void searchButtonAction(ActionEvent event){
        String usernameToSearch = usernameToSearchTextField.getText();
        if(Gettings.userExists(usernameToSearch)){
            Stage peopleProfileStage= Showings.showPeopleProfile(this,usernameToSearch, myUsername);
            peopleProfileStage.setOnHiding(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            System.out.println("Application Closed by click to Close Button(X)");
                        }
                    });
                }
            });
        }else{
            userNotFoundLabel.setText("username not found..");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
