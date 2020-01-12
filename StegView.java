
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StegView extends Application {
	private static final int IMG_WIDTH = 400;
	private static final int IMG_HEIGHT = 300;
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 900;
	
	private GridPane mainGrid, hideGrid, extractGrid;
	private Button startButton, exitButton, hideImageButton, hideMessageButton, hideButton, resetButton;
	private TextField imageField;
	private TextArea messageArea;
//	private Rectangle beforeRect, afterRect;
//	private ImageView beforeImage, afterImage;

	// BUTTONS, FIELDS, ETC MUST BE INIT WITHIN START METHOD
	public StegView() {
    	// Init GridPanes
		mainGrid = new GridPane();
        mainGrid.setHgap(15);
        mainGrid.setVgap(15);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));
        mainGrid.setAlignment(Pos.CENTER);
        
        hideGrid = new GridPane();
//        hideGrid.setGridLinesVisible(true);
        hideGrid.setHgap(10);
        hideGrid.setVgap(10);
        hideGrid.setPadding(new Insets(25, 35, 25, 25));
        hideGrid.setAlignment(Pos.CENTER);
        
        extractGrid = new GridPane();
        extractGrid.setAlignment(Pos.CENTER);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Init Buttons
        startButton = new Button("Start");
        exitButton = new Button("Exit");
        hideImageButton = new Button("Browse");
        hideImageButton.setMinWidth(84);
        hideMessageButton = new Button("Select file");
        hideMessageButton.setDisable(true);
        hideButton = new Button("Hide message");
        hideButton.setMinSize(100, 50);
        hideButton.setFont(Font.font(Font.getDefault().getName(), FontWeight.EXTRA_BOLD, 15));
        
        // Init TextField
        imageField = new TextField();
        
        // Init TextArea
        messageArea = new TextArea();
		
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
		TabPane pane = new TabPane();
		
		/* Set up hideGrid */
		Text hideTitle = new Text("Hide a message");
		hideTitle.setFont(Font.font("Consolas", 30));
		hideGrid.add(hideTitle, 0, 0, 2, 1);
		
		Label imageLabel = new Label("Select image:");
		imageLabel.setWrapText(true);
		hideGrid.add(imageLabel, 0, 1);
		
		Label messageLabel = new Label("Write message:");
		messageLabel.setWrapText(true);
		hideGrid.add(messageLabel, 0, 2);
		
		hideGrid.add(imageField, 1, 1);
		hideGrid.add(messageArea, 1, 2);
		hideGrid.add(hideImageButton, 2, 1);
		hideGrid.add(hideMessageButton, 2, 2);
		
		// Place CheckBox
		CheckBox fileCheckBox = new CheckBox("Use .txt file");
		HBox fileBox = new HBox();
		fileBox.setAlignment(Pos.BOTTOM_RIGHT);
		fileCheckBox.setOnAction((ActionEvent e) -> {
			if(fileCheckBox.isSelected())
				hideMessageButton.setDisable(false);
			else
				hideMessageButton.setDisable(true);
		});
		fileBox.getChildren().add(fileCheckBox);
		hideGrid.add(fileBox, 1, 3);
		
		Rectangle beforeRect = new Rectangle(IMG_WIDTH, IMG_HEIGHT);
		beforeRect.setStroke(Color.BLACK);
		beforeRect.setFill(Color.TRANSPARENT);
		hideGrid.add(beforeRect, 3, 1);
		
		Rectangle afterRect = new Rectangle(IMG_WIDTH, IMG_HEIGHT);
		afterRect.setStroke(Color.BLACK);
		afterRect.setFill(Color.TRANSPARENT);
		hideGrid.add(afterRect, 3, 2);
		
		HBox hideBox = new HBox();
		hideBox.setAlignment(Pos.BOTTOM_RIGHT);
		hideBox.getChildren().add(hideButton);
		hideGrid.add(hideBox, 1, 5);
		
		/* Set up extractGrid */
		Text extractText = new Text("Extract a message");
		extractText.setFont(Font.font("Consolas", 30));
		extractGrid.add(extractText, 0, 0);
		
		Tab hideTab = new Tab("Hide", hideGrid);
		hideTab.setClosable(false);
		
		Tab extractTab = new Tab("Extract", extractGrid);
		extractTab.setClosable(false);
		
		pane.getTabs().addAll(hideTab, extractTab);
		return pane;
	}
}