/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package loginpage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    
}

