/**
 * Created by noyz on 6/22/17.
 */

import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class HomeCell extends ListCell<Post> {
    HBox hbox = new HBox();
    VBox firstPostInfoVBox = new VBox();
    VBox likeVBox = new VBox();
    Label caption = new Label("(empty)");
    Label owner = new Label("(empty");
    Pane pane = new Pane();
    Button likeButton = new Button();
    Button unlikeButton = new Button();
    TextField commentTextField = new TextField();
    Button commentButton= new Button();
    Text comments = new Text();
    Text dateCreated = new Text();
    Text likeCount = new Text();
    ImageView imageView = new ImageView();
     Post lastItem;
    User myUser;
    public HomeCell(User myUser) {
        super();
        this.myUser=myUser;
        firstPostInfoVBox.getChildren().addAll(imageView, pane, caption,owner,dateCreated, likeCount, comments,commentTextField,commentButton);
        likeVBox.getChildren().addAll(likeButton,unlikeButton);
        hbox.getChildren().addAll(firstPostInfoVBox,likeVBox);
        HBox.setHgrow(pane, Priority.ALWAYS);
        commentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(Validations.isValidComment(commentTextField.getText())){
                    NPComment newComment = new NPComment(commentTextField.getText(),myUser.userFirstInfo.username);
                    comments.setText(comments.getText().toString()+"\n"+newComment.username+ " said : "+ newComment.text);
                    Post newPost = new Post(lastItem);
                    newPost.comments.add(newComment);
                    User postUser = Gettings.getUser(owner.getText());
                  /*  Iterator<Post> postIterator = postUser.posts.iterator();
                    while(postIterator.hasNext()){
                        System.out.println(postIterator.next().hashCode());
                    }
                    System.out.println("fasfas"+ lastItem.hashCode());
                    if(postUser.posts.contains(lastItem)){
                        System.out.println("IT DID!!!");
                        //we have work here!
                    }*/
                  if(postUser.removePostByHashCode(lastItem.hashCode())){
                      System.out.println("YEPPIE!!");
                      commentTextField.setText("");
                      postUser.posts.add(newPost);
                      //write user
                      Gettings.writeUser(postUser.userFirstInfo.username,postUser);
                  }

                }
            }
        });
        likeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(lastItem.likes.contains(myUser.userFirstInfo.username))
                    return;
                Post newPost = new Post(lastItem);
                newPost.likes.add(myUser.userFirstInfo.username);
                User postUser = Gettings.getUser(owner.getText());
                if(postUser.removePostByHashCode(lastItem.hashCode())){
                    likeButton.setVisible(false);
                    unlikeButton.setVisible(true);
                    likeCount.setText("likes: "+ new Integer(newPost.likes.size()).toString());
                    System.out.println("YESSS");
                    postUser.posts.add(newPost);
                    Gettings.writeUser(postUser.userFirstInfo.username,postUser);
                }
            }
        });
        unlikeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!lastItem.likes.contains(myUser.userFirstInfo.username))
                    return;
                Post newPost = new Post(lastItem);
                newPost.likes.remove(myUser.userFirstInfo.username);
                User postUser = Gettings.getUser(owner.getText());
                if(postUser.removePostByHashCode(lastItem.hashCode())){
                    likeCount.setText("likes: "+ new Integer(newPost.likes.size()).toString());
                    unlikeButton.setVisible(false);
                    likeButton.setVisible(true);
                    System.out.println("YESSS");
                    postUser.posts.add(newPost);
                    Gettings.writeUser(postUser.userFirstInfo.username,postUser);
                }
            }
        });
    }
    void initLikeHBox(Post item){
        likeButton.setVisible(false);
        unlikeButton.setVisible(false);
        if(item.likes.contains(myUser.userFirstInfo.username)){
            unlikeButton.setVisible(true);
        }else{
            likeButton.setVisible(true);
        }
        likeButton.setText("Like!");
        unlikeButton.setText("unLike :(");
    }
    void initFirstPostInfoHBox(Post item){
        likeCount.setText("likes: "+ (new Integer(item.likes.size()).toString()));
        commentButton.setText("post comment");
        dateCreated.setText(item.date.toString());
        caption.setText(lastItem.caption);
        owner.setText(lastItem.ownerUsername);
        Iterator<NPComment> npCommentIterator = item.comments.iterator();
        while(npCommentIterator.hasNext()){
            NPComment npComment = npCommentIterator.next();
            comments.setText(comments.getText().toString()+"\n"+npComment.username+ " said : "+ npComment.text);
        }
        if(lastItem.image!=null) {
            try {
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(lastItem.image));
                Image image = new Image(imageInputStream);
                imageView.setImage(image);
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
            } catch (IOException e) {
                System.out.println("ERROR while reading from image");
                e.printStackTrace();
            }
        }
        System.out.println(lastItem.image);
    }
    @Override
    protected void updateItem(Post item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            initFirstPostInfoHBox(item);
            initLikeHBox(item);
            setGraphic(hbox);
        }
    }
}
