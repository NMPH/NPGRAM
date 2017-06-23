import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import sun.awt.shell.ShellFolder;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created by noyz on 6/20/17.
 */
public class ProfileController implements Initializable {
    ProfileController(String username){
        this.username=username;
        user= Gettings.getUser(username);
    }
    User user;
    final Circle clip = new Circle(75, 45, 45);
    @FXML
    Label addPostLabel;
    @FXML
    Hyperlink pendingFollowRequests;
    @FXML Label bioLabel;
    @FXML
    public Label UsernameTitle;
    public String username;
    @FXML
    ImageView profilePicture;
    @FXML
    Button editProfileButton;
    @FXML
    Label searchLabel,postLabel,followersLabel,followingsLabel, homeLabel;
    @FXML
    StackPane postsPane;
    public void showChooseChats(Event event) {
        try {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new ChooseChatController(username, primaryStage);
            });
            Parent root = loader.load(getClass().getResource("ChooseChat.fxml").openStream());
            ChooseChatController chooseChatController = (ChooseChatController) loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Problem in showChooseChats");
            e.printStackTrace();
        }
    }
    public void showFollowers(){
        Stage primaryStage = new Stage();
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 300, 150);
        primaryStage.setScene(scene);
        ObservableList<String > list = FXCollections.observableArrayList();
        Iterator<String> followersUsernames = Gettings.getUser(username).followersUsernames.iterator();
        while (followersUsernames.hasNext()){
            list.add(followersUsernames.next());
        }
        ListView<String> lv = new ListView<>(list);
        lv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new UsersCell(user);
            }
        });
        pane.getChildren().add(lv);
        primaryStage.show();
    }
    public void showFollowings(){
        Stage primaryStage = new Stage();
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 300, 150);
        primaryStage.setScene(scene);
        ObservableList<String > list = FXCollections.observableArrayList();
        Iterator<String> followingsIterator = Gettings.getUser(username).followingsUsernames.iterator();
        while (followingsIterator.hasNext()){
            list.add(followingsIterator.next());
        }
        ListView<String> lv = new ListView<>(list);
        lv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new UsersCell(user);
            }
        });
        pane.getChildren().add(lv);
        primaryStage.show();
    }
    public void showMyPosts(){
        ObservableList<Post> list = FXCollections.observableArrayList();
        Iterator<Post> postIterator = Gettings.getUser(username).posts.iterator();
        while (postIterator.hasNext()){
            list.add(postIterator.next());
        }
        ListView<Post> lv = new ListView<>(list);
        lv.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            @Override
            public ListCell<Post> call(ListView<Post> param) {
                return new HomeCell(user);
            }
        });
        postsPane.getChildren().add(lv);
    }
    public void openHome(Event event){
        Stage primaryStage = new Stage();
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 300, 150);
        primaryStage.setScene(scene);
        ObservableList<Post> list = FXCollections.observableArrayList();
        Iterator<String > followingsUsernames= user.followingsUsernames.iterator();
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
                return new HomeCell(user);
            }
        });
        pane.getChildren().add(lv);
        primaryStage.show();
    }
    public void addPost(Event event){
        Stage addPostStage = Showings.showAddPost(this,username);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        addPostStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        Showings.showProfile(this,username);
                    }
                });
            }
        });
    }
    public void PendingFollowRequestsPageOpener(Event event){
        //Showings.showFollowRequestsList(this,username);
        Stage primaryStage = new Stage();
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 300, 150);
        primaryStage.setScene(scene);
        ObservableList<String> list = FXCollections.observableArrayList();
        Iterator<String> followReqs = Gettings.getUser(username).followRequestsRecieved.iterator();
        while(followReqs.hasNext()){
            list.add(followReqs.next());
        }
        ListView<String> lv = new ListView<>(list);
        ProfileController thisProfileController = this;
        lv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new FollowRequestCell(user,thisProfileController);
            }
        });
        pane.getChildren().add(lv);
        primaryStage.show();

    }
    public void search(Event event){

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage searchStage= Showings.showSearch(this,username);
        stage.close();
        searchStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        Showings.showProfile(this,username);
                    }
                });
            }
        });
        //Showings.showSearch(this,username);
    }
    public void editProfilePageOpener(ActionEvent event){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            Showings.showEditProfile(this,username);
    }
    public void setProfilePicture(Event event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select A Photo : ");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"),new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if(file!=null) {
            try {
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(file));
                Image image = new Image(imageInputStream);
                profilePicture.setImage(image);
                profilePicture.setClip(clip);
                user.setProfilePicture(file);
               Gettings.writeUser(username,user);
            } catch (IOException e) {
                System.out.println("ERROR while reading from image");
                e.printStackTrace();
            }
        }
        System.out.println(file);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showMyPosts();
        Image profileImage=null;
        try {
            if (user.profilePicture != null) {
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(user.profilePicture));
                profileImage = new Image(imageInputStream);
            }
        }catch (IOException e){
            System.out.println("problem in reading the user profile picture in ProfileController()");
            e.printStackTrace();
        }
        if(profileImage!=null) {
            profilePicture.setImage(profileImage);
            profilePicture.setClip(clip);
        }
        bioLabel.setText(user.bio);

        followersLabel.setText(new Integer(user.followersUsernames.size()).toString());
        followingsLabel.setText(new Integer(user.followingsUsernames.size()).toString());
        postLabel.setText(new Integer(user.posts.size()).toString());
        UsernameTitle.setText(user.userFirstInfo.username);
    }
}
