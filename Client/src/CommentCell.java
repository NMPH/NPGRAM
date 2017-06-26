/**
 * Created by noyz on 6/26/17.
 */
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sun.misc.Cache;

/**
 * Created by noyz on 6/22/17.
 */
public class CommentCell extends ListCell<Integer> {
    HBox hbox = new HBox();
    Text commentText = new Text("(empty)");
    Pane pane = new Pane();
    Button likeComment = new Button("like");
    Integer lastItem;
    User myUser;
    Post post;
    public CommentCell(User myUser,Post post) {
        /*
        profileController is sent in order to change the followingsLabel when user accepts the request
         */
        super();
        this.post=post;
        this.myUser=myUser;
        hbox.getChildren().addAll(commentText, pane, likeComment);
        HBox.setHgrow(pane, Priority.ALWAYS);
        likeComment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NPComment newNPComment = new NPComment(post.comments.get(lastItem));
                int i=lastItem;
                post.comments.remove(i);
                if(likeComment.getText().equals("like")) {
                    newNPComment.likes.add(myUser.userFirstInfo.username);
                    likeComment.setText("unlike");
                }
                else {
                    newNPComment.likes.remove(myUser.userFirstInfo.username);
                    likeComment.setText("like");
                }
                post.comments.add(newNPComment);
                myUser.posts.remove(post);
                myUser.posts.add(post);
                Gettings.writeUser(myUser.userFirstInfo.username,myUser);
                /*
                tu user e khodesh boro followReqReceved esho bar dar bebar tu followers
                tu user e yaru boro az followReqSent bebar tu followings
                 */
            }
        });
    }
    private void initHBOX(int itemIndex){
        likeComment.setMaxWidth(80);
        commentText.setText(myUser.userFirstInfo.username+" : " + post.comments.get(itemIndex).text);
    }
    @Override
    protected void updateItem(Integer itemIndex, boolean empty) {
        super.updateItem(itemIndex, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = itemIndex;
            setGraphic(hbox);
            initHBOX(itemIndex);
        }
    }
}
