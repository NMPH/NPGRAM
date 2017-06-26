/**
 * Created by noyz on 6/26/17.
 */
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.HashSet;

public class ServerUserInformationCell extends ListCell<String>{
    HBox hbox = new HBox();
    VBox vbox = new VBox();
    Pane pane = new Pane();
    Label username = new Label("empty");
    Label fullName = new Label();
    Label postsCount = new Label();
    VBox imageBox = new VBox();
    ImageView profilePicture = new ImageView();
    String lastItem;
    HashSet<User> usersFile;
    public ServerUserInformationCell(HashSet<User> usersFile){
        super();
        this.usersFile= usersFile;
        imageBox.getChildren().addAll(profilePicture);
        vbox.getChildren().addAll(username,fullName,postsCount);
        hbox.getChildren().addAll(vbox,imageBox);
        HBox.setHgrow(pane, Priority.ALWAYS);

    }
    private void initVboxs(){
        username.setText("username : "+ lastItem);
        User user = User.getUserFromHashSet(usersFile,lastItem);
        fullName.setText("Full Name : "+ user.userFirstInfo.fullName);
        postsCount.setText("Number of Posts: "+ new Integer(user.posts.size()).toString());
        //profilePicture.setImage();
    }
    @Override
    protected void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            username.setText(item);
            initVboxs();
            setGraphic(hbox);
        }
    }
}
