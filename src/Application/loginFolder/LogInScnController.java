package Application.loginFolder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInScnController {

    @FXML
    private Button LogInBtn;

    @FXML
    private TextField emailTextBox;

    @FXML
    private PasswordField passwordTextBox;

    @FXML
    void BtnLogIn(ActionEvent event) {
        String username = emailTextBox.getText();
        String password = passwordTextBox.getText();

        Stage logInWindow = (Stage) emailTextBox.getScene().getWindow();
        logInWindow.close();
    }

}
