import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.awt.image.BufferedImage;

/**
 * Created by noyz on 6/23/17.
 */
public class ChatCell extends ListCell<ChatMessage> {
    HBox hbox = new HBox();
    Pane pane = new Pane();
    Text messageText = new Text();
    ImageView imageView = new ImageView();
    ChatMessage lastItem;
    public ChatCell(){
        super();
        hbox.getChildren().addAll(messageText, pane, imageView);
        HBox.setHgrow(pane, Priority.ALWAYS);

    }
    @Override
    protected void updateItem(ChatMessage chatMessage, boolean empty) {
        super.updateItem(chatMessage, empty);
        setText(null);  // No text in label of super class
        imageView.setVisible(false);
        if (empty) {
            setGraphic(null);
        } else {
            User toChatUser = Gettings.getUser(chatMessage.owner);
            messageText.setText(chatMessage.text);
            if( ImageFunctions.ByteArrayToBufferedImage(chatMessage.image)!=null) {
                imageView.setVisible(true);
                imageView.setImage(SwingFXUtils.toFXImage( ImageFunctions.ByteArrayToBufferedImage(chatMessage.image), null ));
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);
            }
            setGraphic(hbox);
        }
    }
}
