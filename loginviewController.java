/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package loginpage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 *
 * @author zaman
 */
 public class loginviewController implements Initializable {


    @FXML
    private Label label;

    @FXML
    private Label lblCreateAccount;

    @FXML
    private Label lblEntreprenur;

    @FXML
    private Label lblInvestor;

    @FXML
    private Label lblStatus;

    @FXML
    private Pane slidingPane;

    @FXML
    private Tab tabEntreprenur;

    @FXML
    private Tab tabInvestor;

    @FXML
    private TabPane tabPanLogin;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private StackPane rootPane;

    @FXML
    private Button entreprenur_login;

    @FXML
    private Button investor_login;

    @FXML
    private PasswordField login_epassword;

    @FXML
    private TextField login_eusername;

    @FXML
    private PasswordField login_ipassword;

    @FXML
    private TextField login_iusername;

    //for database

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);   // removes ugly default header
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void login(String username, String password, String fxmlPath, ActionEvent event) {

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill all the fields", Alert.AlertType.ERROR);
            return;
        }

        //Database Usage
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection conn = registerpage.database.connectDb();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                showAlert("Information", "Login Successful", Alert.AlertType.INFORMATION);
                Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } else {
                showAlert("Error", "Wrong username or password", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void e_login(ActionEvent event) {
        login(login_eusername.getText(), login_epassword.getText(),"edashboard.fxml",event);

    }

    public void i_login(ActionEvent event) {
        login(login_iusername.getText(), login_ipassword.getText(),"idashboard.fxml",event);
    }



    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    

    @FXML
    private void openInvestorTab(MouseEvent event) {

        TranslateTransition toLeftTransition=new TranslateTransition(new Duration(500), lblStatus);
        toLeftTransition.setToX(slidingPane.getTranslateX());
        toLeftTransition.play();
        toLeftTransition.setOnFinished( event2 -> {
            lblStatus.setText("Investor");
        });
        tabPanLogin.getSelectionModel().select(tabInvestor);
    }

    @FXML
    private void openEntreprenurTab(MouseEvent event) {
        //For usertype slide animation
        TranslateTransition toRightAnimation= new TranslateTransition(new Duration(500), lblStatus);
        toRightAnimation.setToX(slidingPane.getTranslateX()+(slidingPane.getPrefWidth()-lblStatus.getPrefWidth()));
        toRightAnimation.play();
        toRightAnimation.setOnFinished(event1 -> {
            lblStatus.setText("Entreprenur");
        });
        // switch investor to Entreprenur after Sliding
        tabPanLogin.getSelectionModel().select(tabEntreprenur);
    }



    @FXML
    private void openRegisterScene (MouseEvent event) throws IOException {
        //Creating fancy slide transition to sswitch between scenes
        //Load the Create FXML file first, and assign Parent variable
        Parent root = FXMLLoader.load(getClass().getResource ("/registerpage/register.fxml")); //Get current scene
        Scene loginScene = lblCreateAccount.getScene();


        //Place the new scene at the bottom of current scene
        root.translateYProperty().set(loginScene.getHeight());//Add it to our holder pain, which is stack pane.

        rootPane.getChildren().add(root);

       //We create sliding up animation using timeline,
        Timeline timeline = new Timeline ();

        //Use ease in interpolator to have smooth transition
        KeyValue keyValue = new KeyValue (root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame=new KeyFrame (Duration.seconds (2), keyValue);

        //add keyframe to animation
        timeline.getKeyFrames ().add(keyFrame);

        //play the animation
        timeline.play();
        //remove old anchorpanEventHandler <ActionEvent> value plete
        timeline.setOnFinished ((ActionEvent event2)->{
         //remove node from stackpane
            rootPane.getChildren().remove (anchorPane);});
    

    }

}