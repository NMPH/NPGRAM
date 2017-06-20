import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    User user;
    @FXML
    Label exitEditProfileLabel;
    @FXML
    Label DoneLabel;
    @FXML
    TextField nameLabel,usernameLabel,bioLabel;
    public void exitEditProfileLabelclick(Event event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void DoneLabelClick(Event event){
        String name = nameLabel.getText();
        String username = usernameLabel.getText();
        String bio = bioLabel.getText();
    }
    public void setUsernameLabel(String username){
        usernameLabel.setText(username);
    }
   /* public void setFullnameLabel(String username){
        bioLabel.setText(username);
    }*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Socket server = new Socket("127.0.0.1", 1234);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
           // objectOutputStream.writeObject("");
        }catch (IOException e){
            System.out.println("editProfile couldn't connect to server :(");
            e.printStackTrace();
        }

    }
}
