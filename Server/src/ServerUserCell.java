import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Created by noyz on 6/25/17.
 */
public class ServerUserCell extends ListCell<String>{
    HBox hbox = new HBox();
    Pane pane = new Pane();
    Label username = new Label("empty");
    String lastItem;
    public ServerUserCell(){
        super();
        hbox.getChildren().addAll(username);
        HBox.setHgrow(pane, Priority.ALWAYS);

    }
    @Override
    protected void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            username.setText(item);
            setGraphic(hbox);
        }
    }
}
