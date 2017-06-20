import java.io.IOException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by noyz on 6/18/17.
 */
public class Validations {
    public static boolean isValidUserName(String userName) {
        try {
            Socket server = new Socket("127.0.0.1", 1234);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(server.getInputStream());
            objectOutputStream.writeObject("get users");
            objectOutputStream.flush();
            HashSet<String> usernames = ((HashSet<String>) objectInputStream.readObject());
            Iterator<String> usernameIterator = usernames.iterator();
            while(usernameIterator.hasNext()){
                if(usernameIterator.next().equals(userName))
                    return false;
            }

        }catch (IOException e){
            System.out.println("validation problem username");
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        //also not duplicated should be checked
        if (userName.length() < 4)
            return false;
        if (userName.contains(" "))
            return false;
        return true;
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 6)
            return false;
        if (password.contains(" "))
            return false;
        return true;
    }
    public static boolean isValidFullName(String fullName) {
        fullName = fullName.trim();
        String[] splittedName = fullName.split(" ");
        for (String str : splittedName) {
            if (!str.matches("[a-zA-Z]+")) {
                return false;
            }
        }

        return true;
    }
}
