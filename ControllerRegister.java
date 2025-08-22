package registerpage;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Duration;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerRegister implements Initializable {


    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private AnchorPane ancorPane;
    @FXML
    private Label lblLogin;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField email;

    @FXML
    private TextField fullname;


    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_username;

//for database

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    //Email checking

    public boolean emailcheck(){

        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        Matcher match = pattern.matcher(email.getText());

        if(match.find() && match.group().equals(email.getText())){
            return true;
        }
        else{

            Alert alert;
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong Format");
            alert.setContentText("Your email address is Invalid ");
            alert.showAndWait();

            return false;
        }
    }



    public void register() {

        String sql = "INSERT INTO admin (fullname,email,username,password,role) VALUES (?,?,?,?,?)";
        String sql1 = "SELECT * FROM admin WHERE username = ?";
        String sql2 = "SELECT email FROM admin WHERE email = ?";
        connect = database.connectDb();

        try {
            Alert alert;

            if (fullname.getText().isEmpty() || email.getText().isEmpty()
                    || register_username.getText().isEmpty() || register_password.getText().isEmpty()
                    || roleComboBox.getValue().isEmpty()) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Information missing");
                alert.setContentText("Please fill all the fields");
                alert.showAndWait();

            } else if (register_password.getText().length() < 8) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Minimum Password 8 Characters");
                alert.setContentText("Password too short");
                alert.showAndWait();

            } else {

                if (emailcheck()) {

                    // ---- EMAIL DUPLICATE CHECK ----
                    prepare = connect.prepareStatement(sql2);
                    prepare.setString(1, email.getText());
                    result = prepare.executeQuery();

                    if (result.next()) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText(email.getText() + " Already Exist, We don't allow multiple accounts from same mail. Try Different");
                        alert.showAndWait();

                    } else {
                        // ---- USERNAME DUPLICATE CHECK ----
                        prepare = connect.prepareStatement(sql1);
                        prepare.setString(1, register_username.getText());
                        result = prepare.executeQuery();

                        if (result.next()) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText(register_username.getText() + " Already Exist Try different username");
                            alert.showAndWait();

                        } else {
                            // ❌ ERROR: you were calling prepare.execute() here while prepare = sql1 (SELECT)
                            // ✅ FIX: use INSERT query and executeUpdate()

                            prepare = connect.prepareStatement(sql); // switch to INSERT
                            prepare.setString(1, fullname.getText());
                            prepare.setString(2, email.getText());
                            prepare.setString(3, register_username.getText());
                            prepare.setString(4, register_password.getText());
                            prepare.setString(5, roleComboBox.getValue());

                            prepare.executeUpdate(); // insert data properly

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText("Process Complete!");
                            alert.setContentText("Register Successfully");
                            alert.showAndWait();

                            // clear fields
                            fullname.setText("");
                            email.setText("");
                            register_username.setText("");
                            register_password.setText("");
                            roleComboBox.setValue(null);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleComboBox.getItems().addAll("Investor", "Entrepreneur");

    }
    @FXML
    private void openLoginScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/loginpage/loginview.fxml"));
        Scene loginScene = lblLogin.getScene();

        //Place the new scene at the bottom of current scene
        root.translateYProperty().set(loginScene.getHeight()); //Add it to our holder pain, which is stack pane.
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
            rootPane.getChildren().remove (ancorPane);});
    }

}
