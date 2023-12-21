package sample;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import sample.logic.Data;
import sample.logic.Logic;
import sample.logic.PassGenerator;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public static final String YOUR_PASSWORD_WILL_BE_HERE = "Your password will be here";
    static final String SHOW_TEXT = "Show";
    static final String SERVICE_TEXT = "Password of service";
    boolean isCorrectPassword;
    List<String> logins;
    List<String> decryptedLogins;
    List<String> passwords;
    List<String> listView;
    Logic logic;
    Data data = new Data();
    Validator validator = new Validator();
    String lastPassword = null;
    int selectedLogin;
    @FXML
    private MFXPasswordField mainPassId;
    @FXML
    private Button confirmId;
    @FXML
    private ListView<String> listId;
    @FXML
    private MFXPasswordField lastPassId;
    @FXML
    private Button copyId;
    @FXML
    private TextField serviceId;
    @FXML
    private MFXPasswordField servPassId;
    @FXML
    private ToggleButton showId;
    @FXML
    private Button updateId;
    @FXML
    private TextField loginId;
    @FXML
    private Button copyLoginId;
    @FXML
    private TextField searchId;
    @FXML
    private Button saveId;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> mainPassId.requestFocus());
    }

    @FXML
    private void generate() {
        servPassId.setText(PassGenerator.generatePassword());
    }

    @FXML
    private void serviceTab(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            servPassId.requestFocus();
        }
    }

    @FXML
    private void onLastPass() {
        searchId.clear();
    }

    @FXML
    private void searchItem() {
        var chars = searchId.getText();
        selectedLogin = decryptedLogins.stream()
                .filter(x -> x.contains(chars))
                .map(x -> decryptedLogins.indexOf(x)).findFirst().orElseThrow();
        listId.scrollTo(selectedLogin);
        listId.getSelectionModel().select(selectedLogin);
        setPassword();
    }

    @FXML
    private void firstCopyClick() {
        searchId.clear();
        StringSelection stringSelection = new StringSelection(getPassword());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

    }

    @FXML
    private void showClick() {
        lastPassword = getPassword();
        if (showId.isSelected()) {
            lastPassId.setPromptText(lastPassword);
            lastPassId.clear();
            showId.setText("Hide");
            searchId.clear();
        } else {
            lastPassId.setText(lastPassword);
            lastPassId.setPromptText(YOUR_PASSWORD_WILL_BE_HERE);
            lastPassword = null;
            showId.setText(SHOW_TEXT);
        }
    }

    @FXML
    private void listServicesClick() {
        selectedLogin = listId.getSelectionModel().getSelectedIndex();
        setPassword();
        searchId.clear();
    }

    @FXML
    private void update() {
        String updatedPassword = logic.encrypt(lastPassId.getText());
        data.updatePassword(selectedLogin, updatedPassword);
        passwords = data.getEncryptedPasses();
        updateId.setDisable(true);
        showId.setDisable(false);
        listId.setCursor(Cursor.DEFAULT);
    }

    @FXML
    private void activateUpdate() {
        updateId.setDisable(false);
        showId.setSelected(false);
        showId.setText(SHOW_TEXT);
        showId.setDisable(true);
    }

    @FXML
    private void updateEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            update();
        } else {
            activateUpdate();
        }
    }

    @FXML
    private void enterOnMainPassword(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            confirm();
            serviceId.requestFocus();
        }
    }

    @FXML
    private void keyOnServicePassword(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            saveData();
        }
        if (event.getCode() == KeyCode.TAB) {
            saveId.requestFocus();
        }
    }

    @FXML
    private void saveData() {
        data.writeLoginPassToData(logic.encrypt(serviceId.getText()), logic.encrypt(servPassId.getText()));
        logins = data.getEncryptedLogins();
        listView.clear();
        listView.addAll(logins.stream().map(logic::decrypt).toList());
        passwords = data.getEncryptedPasses();
        decryptedLogins = logins.stream().map(logic::decrypt).toList();
        servPassId.setPromptText(SERVICE_TEXT);
        serviceId.clear();
        servPassId.clear();
    }

    @FXML
    private void confirm() {
        var mainPassword = mainPassId.getText();
        logic = Logic.getInstance(mainPassword);
        listView = listId.getItems();

        if (validator.isValidPassword(mainPassword)) {
            mainPassId.clear();
            logins = data.getEncryptedLogins();
            passwords = data.getEncryptedPasses();

            try {
                decryptedLogins = logins.stream().map(logic::decrypt).toList();
                listView.addAll(decryptedLogins);
                confirmId.setDisable(true);
                mainPassId.setDisable(true);
                searchId.setDisable(false);
            } catch (EncryptionOperationNotPossibleException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("What are you doing?");
                alert.setHeaderText("Password does not match");
                alert.setContentText("It looks like you forgot your password");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("What are you doing?");
            alert.setHeaderText("Password does not match");
            alert.setContentText("Should be pretty strong");
            alert.showAndWait();
        }
    }

    @FXML
    private void copyLogin() {
        String loginField = loginId.getText();
        searchId.clear();
        String login = loginField.contains("{")
                ? loginField.substring(loginField.indexOf("{") + 1, loginField.indexOf("}")) : loginField;
        StringSelection stringSelection = new StringSelection(login);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        copyLoginId.setDisable(true);
    }

    @FXML
    private void listServiceKey(KeyEvent event) {
        selectedLogin = listId.getSelectionModel().getSelectedIndex();
        if (event.getCode().equals(KeyCode.DELETE)) {
            data.deleteLoginPass(selectedLogin);
            logins = data.getEncryptedLogins();
            listView.clear();
            decryptedLogins = logins.stream().map(logic::decrypt).toList();
            listView.addAll(decryptedLogins);
            passwords = data.getEncryptedPasses();
            lastPassId.clear();
            lastPassId.setPromptText(YOUR_PASSWORD_WILL_BE_HERE);
            loginId.clear();
            searchId.clear();
        }
        if (event.getCode().equals(KeyCode.UP) || event.getCode().equals(KeyCode.DOWN)) {
            setPassword();
        }
    }

    private String getPassword() {
        return logic.decrypt(passwords.get(selectedLogin));
    }

    private void setPassword() {
        lastPassId.setText(getPassword());
        loginId.setText(listId.getSelectionModel().getSelectedItem());
        copyLoginId.setDisable(false);
        showId.setDisable(false);
        copyId.setDisable(false);
        showId.setSelected(false);
        showId.setText(SHOW_TEXT);
    }
}
