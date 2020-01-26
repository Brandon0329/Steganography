
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StegView extends Application {
	private static final int IMG_WIDTH = 400;
	private static final int IMG_HEIGHT = 225;
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 900;
	
	private GridPane mainGrid, hideGrid, extractGrid;
	private Button startButton, exitButton, hideImageButton, hideMessageButton, hideButton/*, resetButton */;
	private TextField imageField, nameImageField;
	private TextArea messageArea;
	private File currentFile = null;
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
		hideGrid.setPadding(new Insets(15, 35, 15, 25));
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
		nameImageField = new TextField();
		nameImageField.setDisable(true);

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
			primaryStage.setScene(new Scene(setupTabPane(primaryStage), WIDTH, HEIGHT));
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
	
	private TabPane setupTabPane(Stage primaryStage) {
		TabPane pane = new TabPane();
		
		/* Set up hideGrid */
		Text hideTitle = new Text("Hide a message");
		hideTitle.setFont(Font.font("Consolas", 25));
		hideGrid.add(hideTitle, 0, 0, 2, 1);
		
		Label imageLabel = new Label("Select image:");
		imageLabel.setWrapText(true);
		hideGrid.add(imageLabel, 0, 1);
		
		Label messageLabel = new Label("Write message:");
		messageLabel.setWrapText(true);
		hideGrid.add(messageLabel, 0, 3);
		
		Label nameImageLabel = new Label("Provide output image name:");
		nameImageLabel.setWrapText(true);
		hideGrid.add(nameImageLabel, 0, 5);
		
		Label inputLabel = new Label("Input Image:");
		inputLabel.setFont(Font.font("Consolas", FontWeight.BOLD, Font.getDefault().getSize()));
		hideGrid.add(inputLabel, 3, 2);
		
		Label outputLabel = new Label("Output Image:");
		outputLabel.setFont(Font.font("Consolas", FontWeight.BOLD, Font.getDefault().getSize()));
		hideGrid.add(outputLabel, 3, 4);
		
		hideGrid.add(imageField, 1, 1);
		hideGrid.add(messageArea, 1, 3);
		hideGrid.add(nameImageField, 1, 5);
		hideGrid.add(hideImageButton, 2, 1);
		
		hideMessageButton.setOnAction((ActionEvent e) -> {
			File file = new FileChooser().showOpenDialog(primaryStage);
			try {
				Scanner input = new Scanner(file);
				StringBuilder text = new StringBuilder();
				while(input.hasNextLine())
					text.append(input.nextLine() + "\n");
				messageArea.setText(text.toString());
				input.close();
			} catch (FileNotFoundException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Opening Text File");
				alert.setHeaderText(null);
				alert.setContentText("Error opening .txt file");
				alert.showAndWait();
			}
		});
		hideGrid.add(hideMessageButton, 2, 3);
		
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
		hideGrid.add(fileBox, 1, 4);
		
		// Place another CheckBox for file naming
		CheckBox nameCheckBox = new CheckBox("Change name");
		HBox nameBox = new HBox();
		nameBox.setAlignment(Pos.BOTTOM_RIGHT);
		nameCheckBox.setOnAction((ActionEvent e) -> {
			if(nameCheckBox.isSelected())
				nameImageField.setDisable(false);
			else {
				nameImageField.setDisable(true);
				if(currentFile != null)
					nameImageField.setText(createNewImgString(currentFile.toString()));
			}
		});
		nameBox.getChildren().add(nameCheckBox);
		hideGrid.add(nameBox, 1, 6);
		
		
		hideImageButton.setOnAction((ActionEvent e) -> {
			currentFile = new FileChooser().showOpenDialog(primaryStage);
			imageField.setText(currentFile.toString());
			nameImageField.clear();
			if(!nameCheckBox.isSelected())
				nameImageField.setText(createNewImgString(imageField.getText()));
			ImageView imageView = new ImageView(new Image("file:///" + currentFile.toString()));
			imageView.setFitWidth(IMG_WIDTH);
			imageView.setFitHeight(IMG_HEIGHT);
			hideGrid.add(imageView, 3, 1);
		});
		
		Rectangle beforeRect = new Rectangle(IMG_WIDTH, IMG_HEIGHT);
		beforeRect.setStroke(Color.BLACK);
		beforeRect.setFill(Color.TRANSPARENT);
		hideGrid.add(beforeRect, 3, 1);
		
		Rectangle afterRect = new Rectangle(IMG_WIDTH, IMG_HEIGHT);
		afterRect.setStroke(Color.BLACK);
		afterRect.setFill(Color.TRANSPARENT);
		hideGrid.add(afterRect, 3, 3);
		
		HBox hideBox = new HBox();
		hideButton.setOnAction((ActionEvent e) -> {
			try {
				BufferedImage newImage = StegController.hide(getMessage(), currentFile, getImageName());
				if(newImage == null)
					throw new IOException();
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Message Hidden");
				alert.setHeaderText(null);
				alert.setContentText("Message successfully hidden. Press OK to continue");
				alert.showAndWait();
				
				ImageView image = new ImageView(new Image("file:///" + getImageName()));
				image.setFitWidth(IMG_WIDTH);
				image.setFitHeight(IMG_HEIGHT);
				hideGrid.add(image, 3, 3);
			} catch (IOException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Steganography Error");
				alert.setHeaderText(null);
				alert.setContentText("There was an error hiding the message!");
				alert.showAndWait();
			}
		});
		hideBox.setAlignment(Pos.BOTTOM_RIGHT);
		hideBox.getChildren().add(hideButton);
		hideGrid.add(hideBox, 1, 11);
		
		/* Set up extractGrid */
		Text extractText = new Text("Extract a message");
		extractText.setFont(Font.font("Consolas", 30));
		extractGrid.add(extractText, 0, 0);
		
		TextArea area = new TextArea();
		extractGrid.add(area, 0, 1);
		
		Button button = new Button("Extract");
		button.setOnAction((ActionEvent e) -> {
			File file = new FileChooser().showOpenDialog(primaryStage);
			try {
				String message = StegController.reveal(file, null);
				if(message.length() == 0)
					throw new IOException();
				area.setText(message);
			} catch (IOException e1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No Message Found");
				alert.setHeaderText(null);
				alert.setContentText("No message found in this image.");
				alert.showAndWait();
			}
		});
		extractGrid.add(button, 0, 2);
		
		Tab hideTab = new Tab("Hide", hideGrid);
		hideTab.setClosable(false);
		
		Tab extractTab = new Tab("Extract", extractGrid);
		extractTab.setClosable(false);
		
		pane.getTabs().addAll(hideTab, extractTab);
		return pane;
	}
	
	private String createNewImgString(String imgName) {
		String dir = imgName.substring(0, imgName.lastIndexOf('\\') + 1);
		String file = imgName.substring(dir.length());
		int rand = (int) (Math.random() * 1_000_000);
		return dir + file.substring(0, file.lastIndexOf('.')) + rand + ".png";
	}
	
	public String getMessage() {
		return messageArea.getText();
	}
	
	public String getImageName() {
		return nameImageField.getText();
	}
}