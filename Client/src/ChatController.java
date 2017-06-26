import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import sun.awt.windows.ThemeReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

import static javafx.application.Platform.exit;

/**
 * Created by noyz on 6/23/17.
 */
public class ChatController implements Initializable {
    Stage primaryStage;
    String myUsername;
    User myUser;
    Chat chat;
    Chat chatToShow;
    String yaruUsername;
    User yaruUser;
    ChatController(Chat chat,Stage stage,String myUsername){
        this.myUsername=myUsername;
        myUser=Gettings.getUser(myUsername);
        this.chat=chat;
        this.primaryStage=stage;
        if(chat.chatter1.equals(myUsername)){
            yaruUsername=chat.chatter2;
        }else{
            yaruUsername=chat.chatter1;
        }
        yaruUser=Gettings.getUser(yaruUsername);
    }
    @FXML
    Label toChatUsername;
    @FXML
    StackPane ChatPane;
    @FXML
    TextField chatMessageTextField;
    @FXML
    Button sendMessage;
    @FXML
    ImageView imageView;
    @FXML
    Button addImageButton;
    byte[] imageBytes;
    final Circle clip = new Circle(75, 45, 45);
    public void addImage(Event event){

        File file =Gettings.getFileChooserImage((Stage) ((Node) event.getSource()).getScene().getWindow());
        if(file!=null) {
            try {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.indexOf(".") + 1, file.getName().length());
                BufferedInputStream imageInputStream = new BufferedInputStream(new FileInputStream(file));
                BufferedImage image = ImageIO.read(imageInputStream);
                imageBytes= ImageFunctions.bufferedImageToByteArray(image,fileExtension);
                imageView.setImage(SwingFXUtils.toFXImage(image, null ));
            } catch (IOException e) {
                System.out.println("ERROR while reading from image");
                e.printStackTrace();
            }
        }
        System.out.println(file);
    }
    public void sendMessage(){
        myUser = Gettings.getUser(myUsername);
        yaruUser = Gettings.getUser(yaruUsername);
        Chat oldChat=null;
        String text = chatMessageTextField.getText();
        ChatMessage chatMessage= new ChatMessage(myUsername,text,imageBytes);
        Iterator<Chat> chatIterator = myUser.chats.iterator();
        while(chatIterator.hasNext()){
            Chat thisChat = chatIterator.next();
            if(thisChat.equals(chat)){
                oldChat=thisChat;
                chatIterator.remove();
                System.out.println("chat removed");
            }
        }
        chatMessage.text=myUsername+ ": "+ chatMessage.text;
        oldChat.chatMessages.add(chatMessage);
        myUser.chats.add(oldChat);
        Gettings.writeUser(myUser.userFirstInfo.username,myUser);
        oldChat=null;
        chatIterator = yaruUser.chats.iterator();
        while(chatIterator.hasNext()){
            Chat thisChat = chatIterator.next();
            if(thisChat.equals(chat)){
                oldChat=thisChat;
                chatIterator.remove();
                System.out.println("chat removed");
            }
        }
        oldChat.chatMessages.add(chatMessage);
        yaruUser.chats.add(oldChat);
        Gettings.writeUser(yaruUser.userFirstInfo.username,yaruUser);
        imageView.setImage(null);
        imageBytes=null;
        chatMessageTextField.setText(null);
        /*
        read textField and add message
         */
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ShowChatsClass showChatsClass = new ShowChatsClass(myUsername,chat,ChatPane,primaryStage);
        Thread t = new Thread(showChatsClass);
        t.start();
        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("Application Closed by click to Close Button(X)");
                        t.interrupt();
                        Showings.showProfile(this,myUsername);
                    }
                });
            }
        });
    }
}

class ShowChatsClass extends Task<Void> {
    String myUsername;
    Chat chat;
    StackPane ChatPane;
    Stage stage;
    ShowChatsClass(String myUsername,Chat chat,StackPane ChatPane,Stage primaryStage){
        this.myUsername=myUsername;
        this.chat=chat;
        this.ChatPane=ChatPane;
        this.stage=primaryStage;
    }
    @Override
    protected Void call() throws Exception {
        Chat chatToShow;
        while(!Thread.interrupted()){
            chatToShow=null;
            System.out.println("hey");
            HashSet<Chat> chats = Gettings.getUser(myUsername).chats;
            Iterator<Chat> chatsIterator =chats.iterator();
            while(chatsIterator.hasNext()){
                //if chatIterator contains yaru!
                Chat thisChat = chatsIterator.next();
                if(thisChat.hashCode()==chat.hashCode()){
                    chatToShow = thisChat;
                    break;
                }
            }
            if(chatToShow==null){
                System.out.println("WTF CHAT IS EMPTY????!");
            }else{
                ObservableList<ChatMessage> list = FXCollections.observableArrayList();
                Iterator<ChatMessage> chatMessageIterator = chatToShow.chatMessages.iterator();
                while (chatMessageIterator.hasNext()){
                    list.add(chatMessageIterator.next());
                }
                ListView<ChatMessage> lv = new ListView<>(list);
                lv.setCellFactory(new Callback<ListView<ChatMessage>, ListCell<ChatMessage>>() {
                    @Override
                    public ListCell<ChatMessage> call(ListView<ChatMessage> param) {
                        return new ChatCell();
                    }
                });
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                       // bar.setProgress(counter / 1000000.0);
                        ChatPane.getChildren().add(lv);

                    }
                });
            }
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                System.out.println("INTRUPPTED EXCEPTION IN SHOWCHAT ChatController");
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }
}