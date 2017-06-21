import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    @FXML
    Label userNotFoundLabel;
    @FXML
    Button searchButton;
    @FXML
    TextField usernameToSearchTextField;
    public void searchButtonAction(ActionEvent event){
        String usernameToSearch = usernameToSearchTextField.getText();
        if(Gettings.userExists(usernameToSearch)){
            Showings.showPeopleProfile(this,usernameToSearch);
        }else{
            userNotFoundLabel.setText("username not found..");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
