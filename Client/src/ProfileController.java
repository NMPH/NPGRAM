import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.awt.shell.ShellFolder;

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
    public void setProfilePicture(Event event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select A Photo : ");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        try {
            BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(file));
            Image image = new Image(imageInputStream);
            profilePicture.setImage(image);
        }catch (IOException e){
            System.out.println("ERROR while reading from image");
            e.printStackTrace();
        }
        System.out.println(file);
    }
}
