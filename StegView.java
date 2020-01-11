
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StegView extends Application {
	private static final int IMG_WIDTH = 400;
	private static final int IMG_HEIGHT = 300;
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 900;
	
	private GridPane mainGrid, hideGrid, extractGrid;
	private Button startButton, exitButton, hideImageButton, hideMessageButton, hideButton;
	private TextField imageField;
	private TextArea messageArea;
//	private Rectangle beforeRect, afterRect;
//	private ImageView beforeImage, afterImage;
	
    public StegView() {
    	// Init GridPanes
        mainGrid = new GridPane();
        mainGrid.setHgap(15);
        mainGrid.setVgap(15);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));
        
        hideGrid = new GridPane();
        hideGrid.setHgap(10);
        hideGrid.setVgap(10);
        hideGrid.setPadding(new Insets(25, 35, 25, 25));
        
        extractGrid = new GridPane();
        
        // Init Buttons
        startButton = new Button("Start");
        exitButton = new Button("Exit");
        hideImageButton = new Button("Browse");
        hideMessageButton = new Button("Select File");
        hideButton = new Button("Hide Message");
        
        // Init TextField
        imageField = new TextField();
        
        // Init TextArea
        messageArea = new TextArea();
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set up mainGrid
		Text title = new Text("NsidSteg 1.0");
		HBox titleBox = new HBox();
		title.setFont(Font.font("Consolas", 30));
		titleBox.setAlignment(Pos.CENTER);
		titleBox.getChildren().add(title);
		mainGrid.add(titleBox, 0, 0);
		
		Text desc = new Text("Hide or extract messages hidden in images");
		HBox descBox = new HBox();
		desc.setFont(Font.font("Consolas", 20));
		descBox.setAlignment(Pos.CENTER);
		descBox.getChildren().add(desc);
		mainGrid.add(descBox, 0, 1);
		
		HBox startBox = new HBox();
		startBox.setAlignment(Pos.CENTER);
		startBox.getChildren().add(startButton);
		startButton.setOnAction((ActionEvent e) -> {
			primaryStage.setScene(new Scene(setupTabPane(), WIDTH, HEIGHT));
		});
		mainGrid.add(startBox, 0, 2);
		
		HBox exitBox = new HBox();
		exitBox.setAlignment(Pos.CENTER);
		exitBox.getChildren().add(exitButton);
		exitButton.setOnAction((ActionEvent e) -> {
			primaryStage.close();
		});
		mainGrid.add(exitBox, 0, 3);
		
		// Set up Stage
		primaryStage.setTitle("NsidSteg 1.0");
		primaryStage.setScene(new Scene(mainGrid, WIDTH, HEIGHT));
		primaryStage.show();
	}
	
	private TabPane setupTabPane() {
		return null;
	}
}