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
        String oldUsername = user.userFirstInfo.username;
        String username = usernameLabel.getText();
        String bio = bioLabel.getText();
        user.bio=bio;
        user.userFirstInfo.username=username;
        user.userFirstInfo.fullName=name;
        try{
            Socket server = new Socket("127.0.0.1", 1234);
            ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
            outputToServer.writeObject("write user");
            outputToServer.flush();
            outputToServer.writeObject(oldUsername);
            outputToServer.writeObject(user);
            //changing profile username
            FXMLLoader loader=new FXMLLoader();
            Pane root=loader.load(getClass().getResource("Profile.fxml").openStream());
            ProfileController profileController=(ProfileController) loader.getController();
            profileController.username=username;
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            /*
            now we should change the user file and also the directory name AND ALSO the username in user file
             */
        }catch (IOException e){
            System.out.println("Problem when trying to writing new new user when DONE label is clicked");
            e.printStackTrace();
        }
    }
    public void setUsernameLabel(String username){
        usernameLabel.setText(username);
        try{
            Socket server = new Socket("127.0.0.1", 1234);
            ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
            outputToServer.writeObject("get user");
            outputToServer.flush();
            outputToServer.writeObject(username);
            outputToServer.flush();
            user = ((User) inputFromServer.readObject());
            nameLabel.setText(user.userFirstInfo.fullName);
        }catch (IOException e){
            System.out.println("editProfile couldn't connect to server :(");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            System.out.println("CLASS NOT FOUND EXCEPTION IN editProfileController");
            e.printStackTrace();
        }
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
            System.out.println("Hi");
            //objectOutputStream.writeObject("");
        }catch (IOException e){
            System.out.println("editProfile couldn't connect to server :(");
            e.printStackTrace();
        }

    }
}
