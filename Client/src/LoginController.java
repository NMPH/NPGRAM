import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by noyz on 6/18/17.
 */
public class LoginController {
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Hyperlink signUp;
    @FXML
    private Button LoginButton;
    @FXML
    private Label label;
    @FXML
    private Label loginFailure;
    public void Login(ActionEvent event) {
        try {
            Socket server = new Socket("127.0.0.1", 1234);
            ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
            outputToServer.writeObject("login");
            outputToServer.writeObject(usernameText.getText());
            outputToServer.writeObject(passwordText.getText());
            outputToServer.flush();
            boolean isLogin= inputFromServer.readBoolean();
            if(isLogin){
                Stage profileStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
                Scene scene = new Scene(root,600,400);
                profileStage.setScene(scene);
                profileStage.show();
                System.out.println("LOGIN!!!");
            }else{
                loginFailure.setText("invalid username or password");
                System.out.println("WRONG INFO");
            }
        } catch (IOException e) {
            System.out.println("Problem while Login");
            e.printStackTrace();
        }
    }
    public void SignUp(ActionEvent event){
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
            Scene scene = new Scene(root,600,400);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            System.out.println("Problem in Sign Up ShowUp");
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void Redirect(String str){
        label.setText(str);
    }
}
