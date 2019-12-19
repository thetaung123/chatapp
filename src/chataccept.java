import networkConnection.Client;
import networkConnection.NetworkConnection;
import networkConnection.Server;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

/**
 * Created by ASUS on 25-May-17.
 */
public class chataccept extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @FXML private Button serverbtn,clientbtn,btn,btn1,minimizebtn;
    @FXML private TextField name,ip,port,name1,port1;
    @FXML private TextArea input;
    @FXML private JFXButton exitbtn;
    @FXML private JFXButton sendbtn;
    @FXML private JFXButton serverbackbtn;
    @FXML private JFXButton clientbackbtn;
    @FXML private VBox vbox;
    @FXML protected ListView<String> messageArea;
    @FXML protected Label label;

    private double xOffset,yOffset;
    private NetworkConnection connection;

     protected ObservableList<String> data = FXCollections.observableArrayList();


    private Server createServer(int port, String name){
        return new Server(port, e->{
            Platform.runLater(()->{
                data.addAll(e.toString()+"\n\n");
                label.setText(name);
                messageArea.setItems(data);
                messageArea.refresh();
            });
        });
    }
    private Client createClient(String ip, int port, String name){
        return new Client(ip,port, e ->{
            Platform.runLater(()->{
                data.addAll(e.toString()+"\n\n");
                label.setText(name);
                messageArea.setItems(data);
                messageArea.refresh();
            });
        });
    }
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("FXML/mainChat.fxml"));
        fxml.setController(this);
        Parent root = fxml.load();


        serverbtn.setOnAction(event -> {
            final Stage stage = new Stage();
            FXMLLoader server = new FXMLLoader(getClass().getResource("FXML/serverInfo.fxml"));
            server.setController(this);
            try {
                Parent root1 = server.load();
                stage.setScene(new Scene(root1));
                stage.setResizable(false);
                serverbackbtn.setOnAction(event1 -> {
                    primaryStage.show();
                    stage.hide();
                });
                stage.show();
                primaryStage.hide();
                btn1.setOnAction(event1 -> {

                    final Stage serverStage = new Stage();
                    try {
                        connection=createServer(Integer.parseInt(port1.getText()),name1.getText());
                        serverStage.setScene(new Scene(createContent(name1.getText())));

                        connection.startConnection();
                        input.setPrefRowCount(10);
                        stage.hide();


                    }  catch (Exception e) {
                        e.printStackTrace();
                    }

                    vbox.setOnMousePressed(e -> {
                        xOffset = serverStage.getX() - e.getScreenX();
                        yOffset = serverStage.getY() - e.getScreenY();
                        vbox.setCursor(Cursor.CLOSED_HAND);
                    });

                    vbox.setOnMouseDragged(e -> {
                        serverStage.setX(e.getScreenX() + xOffset);
                        serverStage.setY(e.getScreenY() + yOffset);

                    });

                    vbox.setOnMouseReleased(e -> {
                        vbox.setCursor(Cursor.DEFAULT);
                    });

                    serverStage.initStyle(StageStyle.UNDECORATED);
                    serverStage.show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        clientbtn.setOnAction(event -> {
            final Stage stage = new Stage();
            FXMLLoader client = new FXMLLoader(getClass().getResource("FXML/clientInfo.fxml"));
            client.setController(this);
            try {
                Parent root1 = client.load();
                stage.setScene(new Scene(root1));
                stage.setResizable(false);
                clientbackbtn.setOnAction(event1 -> {
                    primaryStage.show();
                    stage.hide();
                });
                stage.show();
                primaryStage.hide();
                btn.setOnAction(event1 -> {
                    final Stage clientStage =new Stage();
                    try {
                        connection= createClient(ip.getText(),Integer.parseInt(port.getText()),name.getText());
                        clientStage.setScene(new Scene(createContent(name.getText())));
                        connection.startConnection();
                        stage.hide();

                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                    vbox.setOnMousePressed(e -> {
                        xOffset = clientStage.getX() - e.getScreenX();
                        yOffset = clientStage.getY() - e.getScreenY();
                        vbox.setCursor(Cursor.CLOSED_HAND);
                    });

                    vbox.setOnMouseDragged(e -> {
                        clientStage.setX(e.getScreenX() + xOffset);
                        clientStage.setY(e.getScreenY() + yOffset);

                    });

                    vbox.setOnMouseReleased(e -> {
                        vbox.setCursor(Cursor.DEFAULT);
                    });
                  clientStage.initStyle(StageStyle.UNDECORATED);

                    clientStage.show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    @Override
    public void stop()throws Exception{
        connection.closeConnection();
    }
    private Parent createContent(String name) throws IOException {

        FXMLLoader fxml = new FXMLLoader(getClass().getResource("FXML/chatbox.fxml"));
        fxml.setController(this);
        VBox root = fxml.load();
        sendbtn.setOnMouseClicked(event -> {
            String message = name+"   :  ";
            String to_myself = "me : ";
            to_myself += input.getText();
            message += input.getText();
            input.clear();
            data.addAll(to_myself + "\n\n");
            try {
                connection.send(message);
            } catch (Exception e) {
                data.addAll("Not connected");
            }
        });
        exitbtn.setOnAction(event -> {
            System.exit(0);
        });
        minimizebtn.setOnMouseClicked(event -> {
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true);
        });



        messageArea.setItems(data);
        return root;

    }

}
