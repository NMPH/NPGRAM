import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.awt.shell.ShellFolder;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by noyz on 6/20/17.
 */
public class ProfileController implements Initializable {
    User user;
    @FXML Label bioLabel;
    @FXML
    public Label UsernameTitle;
    public String username;
    @FXML
    ImageView profilePicture;
    @FXML
    Button editProfileButton;

    public void setUsername(String username){
        this.username=username;
        UsernameTitle.setText(username);
    }
   /* public void setFullname(String Fullname){
        this.Fullname=Fullname;
    }*/
    public void editProfilePageOpener(ActionEvent event){
        try {
       /*     Stage editProfileStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("EditProfile.fxml"));
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            editProfileStage.setScene(scene);
            editProfileStage.show();*/
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            Stage primaryStage=new Stage();
            FXMLLoader loader=new FXMLLoader();
            Pane root=loader.load(getClass().getResource("EditProfile.fxml").openStream());
            EditProfileController editProfileController=(EditProfileController) loader.getController();
            editProfileController.setUsernameLabel(username);
            //editProfileController.setFullnameLabel(Fullname);
           // System.out.println(Fullname+"gh");
            Scene scene=new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (IOException e){
            System.out.println("edit profile opening problem");
            e.printStackTrace();
        }
    }
    public void setProfilePicture(Event event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select A Photo : ");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if(file!=null) {
            try {
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(file));
                Image image = new Image(imageInputStream);
                profilePicture.setImage(image);
                user.setProfilePicture(file);
                try{
                    Socket server = new Socket("127.0.0.1", 1234);
                    ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
                    ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
                    outputToServer.writeObject("write user");
                    outputToServer.flush();
                    outputToServer.writeObject(username);
                    outputToServer.writeObject(user);
                }catch(IOException e){
                    System.out.println("Error in setProfilePicture writing Image to Server");
                }
            } catch (IOException e) {
                System.out.println("ERROR while reading from image");
                e.printStackTrace();
            }
        }
        System.out.println(file);
    }
    public void init(){
        try {
            Socket server = new Socket("127.0.0.1", 1234);
            ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
            outputToServer.writeObject("get user");
            outputToServer.flush();
            outputToServer.writeObject(username);
            outputToServer.flush();
            user = ((User) inputFromServer.readObject());
            if(user.profilePicture!=null) {
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(user.profilePicture));
                Image image = new Image(imageInputStream);
                profilePicture.setImage(image);
            }
        }catch (IOException e){
            System.out.println("Error while loading initialize method in Profile");
        }catch (ClassNotFoundException e){
            System.out.println("WTF CLASS NOT FOUND ????! IN INITIALIZE OF PROFILE");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //profilePicture.setImage();
/*        try {
            Socket server = new Socket("127.0.0.1", 1234);
            ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
            outputToServer.writeObject("get_init");

        }catch (IOException e){
            System.out.println("Error while loading initialize method in Profile");
            e.printStackTrace();*/
        }
}
