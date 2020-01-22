import javafx.application.Application;

public class StegMain {
	public static void main(String[] args) {
		StegView view = new StegView();
		Application.launch(view.getClass(), args);
	}
}
