import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import sun.awt.windows.ThemeReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    TextField chatMessage;
    @FXML
    Button sendMessage;
    @FXML
    ImageView imageView;
    public void sendMessage(){
        myUser = Gettings.getUser(myUsername);
        yaruUser = Gettings.getUser(yaruUsername);
        byte[] res=null;
/*        if(imageView.getImage()!=null) {
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                ImageIO.write(bImage, "png", s);
                res = s.toByteArray();
                s.close();
            } catch (IOException e) {
                System.out.println("error while reading image in ChatController ");
                e.printStackTrace();
            }

        }*/
        Chat oldChat=null;
        String text = chatMessage.getText();
        ChatMessage chatMessage= new ChatMessage(myUsername,text,res);
        Iterator<Chat> chatIterator = myUser.chats.iterator();
        while(chatIterator.hasNext()){
            Chat thisChat = chatIterator.next();
            if(thisChat.equals(chat)){
                oldChat=thisChat;
                chatIterator.remove();
                System.out.println("chat removed");
            }
        }
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
        /*
        read textField and add message
         */
    }
    /*public void showChat(){
        while(true){
            HashSet<Chat> chats = myUser.chats;
            Iterator<Chat> chatsIterator =chats.iterator();
            while(chatsIterator.hasNext()){
                //if chatIterator contains yaru!
                Chat thisChat = chatsIterator.next();
                if(thisChat.containsChatter(chat.chatter1)&&(thisChat.containsChatter(chat.chatter2))){
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
                ChatPane.getChildren().add(lv);
            }
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                System.out.println("INTRUPPTED EXCEPTION IN SHOWCHAT ChatController");
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //showChat();
        ShowChatsClass showChatsClass = new ShowChatsClass(myUsername,chat,ChatPane,primaryStage);
        Thread t = new Thread(showChatsClass);
        //t.setDaemon(true);
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
/*    @Override
    public void run() {


    }*/

    @Override
    protected Void call() throws Exception {
        Chat chatToShow=null;
        while(!Thread.interrupted()){
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
                Thread.sleep(100);
            }catch(InterruptedException e){
                System.out.println("INTRUPPTED EXCEPTION IN SHOWCHAT ChatController");
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }
}