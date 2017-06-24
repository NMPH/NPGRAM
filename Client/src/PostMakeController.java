import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by noyz on 6/22/17.
 */
public class PostMakeController {
    User myUser;
    String myUsername;
    byte[] imageFile;
    String caption;
    @FXML
    Button selectImageButton;
    @FXML
    ImageView imageView;
    @FXML
    TextField captionTextField;
    public void submitPost(Event event){
        caption = captionTextField.getText();
        Post post = new Post(imageFile,caption,myUsername);
        myUser.posts.add(post);
        Gettings.writeUser(myUsername,myUser);
        //Showings.showProfile(this,myUsername);
        try {
            Thread.sleep(10);
        }catch (InterruptedException e){
            System.out.println("Interrupted exception in PostMakeController");
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void selectImage(Event event){
        File file =Gettings.getFileChooserImage((Stage) ((Node) event.getSource()).getScene().getWindow());
        if(file!=null) {
            try {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.indexOf(".") + 1, file.getName().length());
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(file));
                BufferedImage image = ImageIO.read(imageInputStream);
                imageFile= ImageFunctions.bufferedImageToByteArray(image,fileExtension);
                imageView.setImage(SwingFXUtils.toFXImage(image, null ));
            } catch (IOException e) {
                System.out.println("ERROR while reading from image");
                e.printStackTrace();
            }
        }
   /*         FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select A Photo : ");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"),new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            imageFile = fileChooser.showOpenDialog(stage);
            if(imageFile!=null) {
                try {
                    BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(imageFile));
                    Image image = new Image(imageInputStream);
                    imageView.setImage(image);
                    // make a post
                } catch (IOException e) {
                    System.out.println("ERROR while reading Image in PostMakeController");
                    e.printStackTrace();
                }
            }
            System.out.println(imageFile);*/
    }
    public PostMakeController(String myUsername){
        this.myUsername = myUsername;
        myUser = Gettings.getUser(myUsername);
    }

}
