import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by noyz on 6/21/17.
 */
public class Showings  {
    public static void showProfile(Object classObject,String username){
        try{
            Stage profileStage = new Stage();
            FXMLLoader loader=new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new ProfileController(username);
            });
            Pane root=loader.load(classObject.getClass().getResource("Profile.fxml").openStream());
            ProfileController profileController=(ProfileController) loader.getController();
            Scene profileScene = new Scene(root,650,400);
            profileScene.getStylesheets().add(classObject.getClass().getResource("style.css").toExternalForm());
            profileStage.setScene(profileScene);
            profileStage.show();
        }catch (IOException e){
            System.out.println("Problem while loading Profile Page");
        }
    }
    public static void showEditProfile(Object classObject,String username){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(c -> {
                return new EditProfileController(username);
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
    public static void showSearch(Object classObject,String myUsername){
        try{
            Stage primaryStage=new Stage();
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
    }
    public static void showPeopleProfile(Object classObject,String usernamePeople,String myUsername){
        try{
            Stage primaryStage=new Stage();
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
        }
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
