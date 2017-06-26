import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by noyz on 6/18/17.
 */
public class SignUpController {
    @FXML
    private Label badLoginInputsLabel;
    @FXML
    private TextField FullNameText;
    @FXML
    private TextField UserNameText;
    @FXML
    private PasswordField PasswordText;
    @FXML
    private Button SignUpButton;

    public void SignUpCheck(ActionEvent event) throws IOException {
        if (!(Validations.isValidUserName(UserNameText.getText())
                && Validations.isValidFullName(FullNameText.getText())
                && Validations.isValidPassword(PasswordText.getText()))) {
            badLoginInputsLabel.setText("Please review your login info");
        } else {
                boolean isSignUpSuccessfull =Gettings.isSignUp(FullNameText.getText(),UserNameText.getText()
                ,PasswordText.getText());
                if (isSignUpSuccessfull) {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                    LoginController loginController =Showings.showLogin(this);
                    loginController.Redirect(UserNameText.getText() + " Signed Up");
                }
            badLoginInputsLabel.setText("");
        }
        //sign up
    }
}
