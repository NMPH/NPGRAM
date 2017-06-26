/**
 * Created by noyz on 6/22/17.
 */

import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class HomeCell extends ListCell<Post> {
    HBox hbox = new HBox();
    VBox firstPostInfoVBox = new VBox();
    VBox likeVBox = new VBox();
    VBox removeVBox = new VBox();
    Button removePostButton = new Button();
    Label caption = new Label("(empty)");
    Label owner = new Label("(empty");
    Pane pane = new Pane();
    Button likeButton = new Button();
    Button unlikeButton = new Button();
    TextField commentTextField = new TextField();
    Button commentButton= new Button();
    Text comments = new Text();
    StackPane commentsPane = new StackPane();
    Text dateCreated = new Text();
    Text likeCount = new Text();
    ImageView imageView = new ImageView();
    Post lastItem;
    User myUser;
    public HomeCell(User myUser) {
        super();
        this.myUser=myUser;
        removeVBox.getChildren().addAll(removePostButton);
        firstPostInfoVBox.getChildren().addAll(imageView, pane, caption,owner,dateCreated, likeCount, commentsPane,commentTextField,commentButton);
        likeVBox.getChildren().addAll(likeButton,unlikeButton);
        hbox.getChildren().addAll(firstPostInfoVBox,likeVBox,removeVBox);
        HBox.setHgrow(pane, Priority.ALWAYS);
        removePostButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myUser.posts.remove(lastItem);
                Gettings.writeUser(myUser.userFirstInfo.username,myUser);
                hbox.setVisible(false);
            }
        });
        likeCount.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage primaryStage = new Stage();
                StackPane pane = new StackPane();
                Scene scene = new Scene(pane, 300, 150);
                primaryStage.setScene(scene);
                ObservableList<String > list = FXCollections.observableArrayList();
                Iterator<String> followingsIterator =lastItem.likes.iterator();
                while (followingsIterator.hasNext()){
                    list.add(followingsIterator.next());
                }
                ListView<String> lv = new ListView<>(list);
                lv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        return new UsersCell(myUser);
                    }
                });
                pane.getChildren().add(lv);
                primaryStage.show();
            }
        });
        commentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                commentsPane.setVisible(true);
                if(Validations.isValidComment(commentTextField.getText())){
                    NPComment newComment = new NPComment(commentTextField.getText(),myUser.userFirstInfo.username);
                    //comments.setText(comments.getText().toString()+"\n"+newComment.username+ " said : "+ newComment.text);
                    Post newPost = new Post(lastItem);
                    newPost.comments.add(newComment);
                    initCommentsPane(newPost);
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
    private void initCommentsPane(Post item){
        commentsPane.setMaxWidth(400);
        commentsPane.setMaxHeight(100);
        ObservableList<Integer> list = FXCollections.observableArrayList();
        Iterator<NPComment > npCommentIterator= item.comments.iterator();
        Integer i=0;
        while(npCommentIterator.hasNext()){
            npCommentIterator.next();
            list.add(i);
            i++;
        }
        ListView<Integer> lv = new ListView<>(list);
        lv.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {
            @Override
            public ListCell<Integer> call(ListView<Integer> param) {
                return new CommentCell(myUser,item);
            }
        });
        commentsPane.getChildren().add(lv);
        if(item.comments.isEmpty())
            commentsPane.setVisible(false);
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
    void initRemoveHBox(Post item){
        removePostButton.setText("Remove Post");
        if(!lastItem.ownerUsername.equals(myUser.userFirstInfo.username))
            removePostButton.setVisible(false);
    }
    void initFirstPostInfoHBox(Post item){
        if(!item.isCommentOpen){
            commentTextField.setVisible(false);
            commentButton.setVisible(false);
        }
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
            BufferedImage profileImage =  ImageFunctions.ByteArrayToBufferedImage(lastItem.image);
            Image card = SwingFXUtils.toFXImage(profileImage, null );
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setImage(card);
            /*try {
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(lastItem.image));
                Image image = new Image(imageInputStream);
                imageView.setImage(image);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
            } catch (IOException e) {
                System.out.println("ERROR while reading from image");
                e.printStackTrace();
            }*/
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
            initRemoveHBox(item);
            initCommentsPane(item);
            setGraphic(hbox);
        }
    }
}
