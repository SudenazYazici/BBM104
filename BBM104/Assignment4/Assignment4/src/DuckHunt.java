import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DuckHunt extends Application {
    public int curserNum = 1;
    public int backgroundNum = 1;
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws FileNotFoundException {
        //creating the image object
        final InputStream[] stream = {new FileInputStream("assets/favicon/1.png")};
        Image image = new Image(stream[0]);
        //Creating the image view
        ImageView imageView = new ImageView();
        //Setting image to the image view
        imageView.setImage(image);
        //Setting the image view parameters
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitWidth(600);
        imageView.setPreserveRatio(true);
        Label label = new Label("PRESS ENTER TO PLAY");
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), label);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();
        //Setting the Scene object
        label.setTranslateX(125);
        label.setTranslateY(350);
        label.setFont(Font.font ("Ariel", FontWeight.BOLD, 40));
        label.setTextFill(Color.ORANGE);
        Label label2 = new Label("PRESS ESC TO EXIT");
        FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(1), label2);
        fadeTransition2.setFromValue(1.0);
        fadeTransition2.setToValue(0.0);
        fadeTransition2.setCycleCount(Animation.INDEFINITE);
        fadeTransition2.play();
        //Setting the Scene object
        label2.setTranslateX(150);
        label2.setTranslateY(400);
        label2.setFont(Font.font ("Ariel", FontWeight.BOLD, 40));
        label2.setTextFill(Color.ORANGE);
        Group root = new Group(imageView);
        root.getChildren().add(label);
        root.getChildren().add(label2);
        TextField field = new TextField();
        root.getChildren().add(field);
        field.setOpacity(0);
        field.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                primaryStage.close();
                Stage secondStage = new Stage();
                final InputStream[] stream2 = {null};
                try {
                    stream2[0] = Files.newInputStream(Paths.get("assets/background/"+backgroundNum+".png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Image image2 = new Image(stream2[0]);
                ImageView imageView2 = new ImageView();
                imageView2.setImage(image2);
                imageView2.setX(0);
                imageView2.setY(0);
                imageView2.setFitWidth(600);
                imageView2.setPreserveRatio(true);

                Image image4 = new Image(stream2[0]);
                ImageView imageView4 = new ImageView();
                imageView4.setImage(image4);
                imageView4.setX(0);
                imageView4.setY(0);
                imageView4.setFitWidth(600);
                imageView4.setPreserveRatio(true);

                Image image3 = new Image("assets/crosshair/"+curserNum+".png");
                Label label3 = new Label("USE ARROW KEYS TO NAVIGATE");
                label3.setTranslateX(120);
                label3.setTranslateY(50);
                label3.setFont(Font.font ("Ariel", FontWeight.BOLD,20));
                label3.setTextFill(Color.ORANGE);
                Label label4 = new Label("PRESS ENTER TO START");
                label4.setTranslateX(150);
                label4.setTranslateY(70);
                label4.setFont(Font.font ("Ariel", FontWeight.BOLD,20));
                label4.setTextFill(Color.ORANGE);
                Label label5 = new Label("PRESS ESC TO EXIT");
                label5.setTranslateX(160);
                label5.setTranslateY(90);
                label5.setFont(Font.font ("Ariel", FontWeight.BOLD,20));
                label5.setTextFill(Color.ORANGE);
                Group root2 = new Group(imageView2);
                root2.getChildren().add(label3);
                root2.getChildren().add(label4);
                root2.getChildren().add(label5);
                Scene scene2 = new Scene(root2, 600, 550);
                scene2.setCursor(new ImageCursor(image3));
                secondStage.setTitle("DuckHunt");
                secondStage.setScene(scene2);
                secondStage.show();
                secondStage.setResizable(false);
                TextField field2 = new TextField();
                root2.getChildren().add(field2);
                field2.setOpacity(0);
                field2.setOnKeyPressed( event2 -> {
                    if (event2.getCode() == KeyCode.ESCAPE){
                        secondStage.close();
                        primaryStage.show();
                    } else if (event2.getCode() == KeyCode.UP) {
                        try{
                            curserNum+=1;
                            scene2.setCursor(new ImageCursor(new Image("assets/crosshair/"+curserNum+".png")));
                        }catch (Exception e){
                            curserNum =1;
                            scene2.setCursor(new ImageCursor(new Image("assets/crosshair/"+curserNum+".png")));
                        }
                    } else if (event2.getCode() == KeyCode.DOWN) {
                        try{
                            curserNum-=1;
                            scene2.setCursor(new ImageCursor(new Image("assets/crosshair/"+curserNum+".png")));
                        }catch (Exception e){
                            curserNum =7;
                            scene2.setCursor(new ImageCursor(new Image("assets/crosshair/"+curserNum+".png")));
                        }
                    } else if (event2.getCode() == KeyCode.RIGHT) {
                        try{
                            backgroundNum += 1;
                            imageView2.setImage(new Image("assets/background/"+backgroundNum+".png"));
                            imageView2.setX(0);
                            imageView2.setY(0);
                            imageView2.setFitWidth(600);
                            imageView2.setPreserveRatio(true);

                            imageView4.setImage(new Image("assets/foreground/"+backgroundNum+".png"));
                            imageView4.setX(0);
                            imageView4.setY(0);
                            imageView4.setFitWidth(600);
                            imageView4.setPreserveRatio(true);
                        }catch (Exception ep){
                            backgroundNum = 1;
                            imageView2.setImage(new Image("assets/background/"+backgroundNum+".png"));
                            imageView2.setX(0);
                            imageView2.setY(0);
                            imageView2.setFitWidth(600);
                            imageView2.setPreserveRatio(true);

                            imageView4.setImage(new Image("assets/foreground/"+backgroundNum+".png"));
                            imageView4.setX(0);
                            imageView4.setY(0);
                            imageView4.setFitWidth(600);
                            imageView4.setPreserveRatio(true);
                        }
                    } else if (event2.getCode() == KeyCode.LEFT) {
                        try{
                            backgroundNum -= 1;
                            imageView2.setImage(new Image("assets/background/"+backgroundNum+".png"));
                            imageView2.setX(0);
                            imageView2.setY(0);
                            imageView2.setFitWidth(600);
                            imageView2.setPreserveRatio(true);

                            imageView4.setImage(new Image("assets/foreground/"+backgroundNum+".png"));
                            imageView4.setX(0);
                            imageView4.setY(0);
                            imageView4.setFitWidth(600);
                            imageView4.setPreserveRatio(true);
                        }catch (Exception ep){
                            backgroundNum = 6;
                            imageView2.setImage(new Image("assets/background/"+backgroundNum+".png"));
                            imageView2.setX(0);
                            imageView2.setY(0);
                            imageView2.setFitWidth(600);
                            imageView2.setPreserveRatio(true);

                            imageView4.setImage(new Image("assets/foreground/"+backgroundNum+".png"));
                            imageView4.setX(0);
                            imageView4.setY(0);
                            imageView4.setFitWidth(600);
                            imageView4.setPreserveRatio(true);
                        }
                    } else if( event.getCode() == KeyCode.ENTER ) {
                        final int[] ammo = {3};
                        label3.setText("Level 1/6");
                        label3.setTranslateX(200);
                        label3.setTranslateY(10);
                        label4.setText("");
                        label5.setText("");
                        Label label6 = new Label("Ammo Left: "+ ammo[0]);
                        label6.setTranslateX(450);
                        label6.setTranslateY(10);
                        label6.setFont(Font.font ("Ariel", FontWeight.BOLD,20));
                        label6.setTextFill(Color.ORANGE);
                        root2.getChildren().add(label6);
                        root2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent t) {
                                MouseButton button = t.getButton();
                                if (button==MouseButton.PRIMARY) {
                                    ammo[0] -=1;
                                    label6.setText("Ammo Left: "+ ammo[0]);


                                }
                            }
                        });
                        ImageView imageView5 = new ImageView();
                        imageView5.setImage(new Image("assets/duck_black/4.png"));
                        root2.getChildren().add(imageView5);
                        PathTransition pt = new PathTransition(Duration.millis(10000),
                                new Line(100, 200, 600, 200), imageView5);
                        pt.play();
                        if (imageView5.getX()==600){
                            PathTransition pt2 = new PathTransition(Duration.millis(10000),
                                    new Line(600, 200, 0, 200), imageView5);
                            pt2.play();
                        }
                    }
                });

            } else if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        } );
        Scene scene = new Scene(root, 600, 550);
        primaryStage.setTitle("DuckHunt"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
        primaryStage.setResizable(false);
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}