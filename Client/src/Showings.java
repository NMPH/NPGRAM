import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by noyz on 6/21/17.
 */
public class Showings  {
    public static void setUserOnline(String username){
            User user = Gettings.getUser(username);
            if(user!=null){
                user.online=true;
                Gettings.writeUser(username,user);
            }
    }
    public static void showProfile(Object classObject,String username){
        try{
            setUserOnline(username);
            Stage profileStage = new Stage();
            FXMLLoader loader=new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new ProfileController(username);
            });
            Pane root=loader.load(classObject.getClass().getResource("Profile.fxml").openStream());
            ProfileController profileController=(ProfileController) loader.getController();
            Scene profileScene = new Scene(root,650,900);
            profileStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            User user =Gettings.getUser(username);
                            if(user!=null) {
                                user.online = false;
                                Gettings.writeUser(username, user);
                                System.out.println("offline");
                            }
                        }
                    });
                }
            });
            profileScene.getStylesheets().add(classObject.getClass().getResource("style.css").toExternalForm());
            profileStage.setScene(profileScene);
            profileStage.show();
        }catch (IOException e){
            System.out.println("Problem while loading Profile Page");
        }
    }
    public static void showEditProfile(Object classObject,String username){
        try {
            setUserOnline(username);
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new EditProfileController(username,primaryStage);
            });
            Pane root = loader.load(classObject.getClass().getResource("EditProfile.fxml").openStream());
            EditProfileController editProfileController = (EditProfileController) loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(classObject.getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
            System.out.println("Problem while loading Showings.showEditProfile");
            e.printStackTrace();
        }

    }
    public static Stage showSearch(Object classObject,String myUsername){
        Stage primaryStage=null;
        try{
            setUserOnline(myUsername);
            primaryStage=new Stage();
            FXMLLoader loader=new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new SearchController(myUsername);
            });
            Parent root=loader.load(classObject.getClass().getResource("Search.fxml").openStream());
            SearchController searchController=(SearchController) loader.getController();
            Scene scene=new Scene(root);
            scene.getStylesheets().add(classObject.getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (IOException e){
            System.out.println("problem in showSearch function in Showings");
        }
        return primaryStage;
    }
    /*public static void showFollowRequestsList(Object classObject,String myUsername){
        try{
            Stage primaryStage=new Stage();
            FXMLLoader loader=new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new FollowRequestsListController(myUsername);
            });
            Pane root=loader.load(classObject.getClass().getResource("FollowRequestsList.fxml").openStream());
            FollowRequestsListController followRequestsListController=(FollowRequestsListController) loader.getController();
            followRequestsListController.pane=root;
            followRequestsListController.init();
            Scene scene=new Scene(root);
            scene.getStylesheets().add(classObject.getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (IOException e){
            System.out.println("problem in showSearch function in Showings");
        }
    }*/
    public static Stage showAddPost(Object classObject,String myUsername){
        Stage primaryStage= null;
        try{
            setUserOnline(myUsername);
            primaryStage=new Stage();
            FXMLLoader loader=new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new PostMakeController(myUsername);
            });
            Parent root=loader.load(classObject.getClass().getResource("PostMake.fxml").openStream());
            PostMakeController postMakeController=(PostMakeController) loader.getController();
            Scene scene=new Scene(root);
            scene.getStylesheets().add(classObject.getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (IOException e){
            System.out.println("problem in showAddPost function in Showings");
            e.printStackTrace();
        }
        return primaryStage;
    }
    public static Stage showPeopleProfile(Object classObject,String usernamePeople,String myUsername){
        Stage primaryStage=null;
        try{
            setUserOnline(myUsername);
            primaryStage=new Stage();
            FXMLLoader loader=new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new PeopleProfileController(usernamePeople, myUsername);
            });
            Pane root=loader.load(classObject.getClass().getResource("PeopleProfile.fxml").openStream());
            PeopleProfileController peopleProfileController=(PeopleProfileController) loader.getController();
            Scene scene=new Scene(root);
            scene.getStylesheets().add(classObject.getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (IOException e){
            System.out.println("problem in search function in PeopleProfileController");
            e.printStackTrace();
        }
        return primaryStage;
    }
    /*public static <ControllerType> void showPage(Object classObject,String username,String fxml, Method controllerConstructor){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new ControllerType(username);
            });
            Pane root = loader.load(classObject.getClass().getResource(fxml).openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add(classObject.getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
            System.out.println("Problem while loading Showings.showEditProfile");
            e.printStackTrace();
        }

    }*/
}
