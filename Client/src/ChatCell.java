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
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = chatMessage;
            User toChatUser = Gettings.getUser(chatMessage.owner);
            messageText.setText(chatMessage.text);
            BufferedImage profileImage =  ImageFunctions.ByteArrayToBufferedImage(chatMessage.image);
            if(profileImage!=null) {
                imageView.setVisible(true);
                Image card = SwingFXUtils.toFXImage(profileImage, null );
                imageView.setImage(card);
            }
            //imageView.setImage(ImageFunctions.getJavaFXImage(chatMessage.image,50,50));
            //toChatUser = Gettings.getUser(chatToUsername);
            //initLabelAndButton(chatToUsername);
            setGraphic(hbox);
        }
    }
}
