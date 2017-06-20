import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.awt.shell.ShellFolder;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by noyz on 6/20/17.
 */
public class ProfileController {
    @FXML
    ImageView profilePicture;
    @FXML
    Button editProfileButton;
    public void editProfilePageOpener(ActionEvent event){
        try {
            Stage editProfileStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("EditProfile.fxml"));
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            editProfileStage.setScene(scene);
            editProfileStage.show();
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
            } catch (IOException e) {
                System.out.println("ERROR while reading from image");
                e.printStackTrace();
            }
        }
        System.out.println(file);
    }
}
