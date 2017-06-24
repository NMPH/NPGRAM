import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created by noyz on 6/23/17.
 */
public class HomeController implements Initializable {
    Stage primaryStage;
    String myUsername;
    User myUser;
    HomeController(String myUsername,Stage stage){
        this.myUsername=myUsername;
        myUser=Gettings.getUser(myUsername);
        this.primaryStage=stage;
    }
    @FXML
    ImageView homeImageView;
    @FXML
    StackPane stackPane;
    public void goHome(Event event){
        primaryStage.close();
        System.out.println("HIHI");
    }
    public void listPosts(){
        ObservableList<Post> list = FXCollections.observableArrayList();
        Iterator<String > followingsUsernames= myUser.followingsUsernames.iterator();
        while(followingsUsernames.hasNext()){
            Iterator<Post> postIterator = Gettings.getUser(followingsUsernames.next()).posts.iterator();
            while (postIterator.hasNext()){
                list.add(postIterator.next());
                //we've got works here!
            }
        }
        ListView<Post> lv = new ListView<>(list);
        lv.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            @Override
            public ListCell<Post> call(ListView<Post> param) {
                return new HomeCell(myUser);
            }
        });
        stackPane.getChildren().add(lv);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("Application Closed by click to Close Button(X)");
                        Showings.showProfile(this,myUsername);
                    }
                });
            }
        });
        listPosts();
        homeImageView.setImage(SwingFXUtils.toFXImage(ImageFunctions.ByteArrayToBufferedImage(Gettings.getHomeIcon()),null));
    }
}
