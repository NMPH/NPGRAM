import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public void SignUpCheck(ActionEvent event) {
        if (!(Validations.isValidUserName(UserNameText.getText())
                && Validations.isValidFullName(FullNameText.getText())
                && Validations.isValidPassword(PasswordText.getText()))) {
            badLoginInputsLabel.setText("Please review your login info");
        } else {
            try {
                Socket server = new Socket("127.0.0.1", 1234);
                ObjectOutputStream outputToServer = new ObjectOutputStream(server.getOutputStream());
                ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());
                outputToServer.writeObject("sign_up");
                outputToServer.writeObject(FullNameText.getText());
                outputToServer.writeObject(UserNameText.getText());
                outputToServer.writeObject(PasswordText.getText());
                outputToServer.flush();
                boolean isSignUpSuccessfull = inputFromServer.readBoolean();
                if (isSignUpSuccessfull) {
                    badLoginInputsLabel.setText("SignUp Successfull!");
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            badLoginInputsLabel.setText("");
        }
        //sign up
    }
}
