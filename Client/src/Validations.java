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
            HashSet<String> usernames =Gettings.getUsers();
            Iterator<String> usernameIterator = usernames.iterator();
            while(usernameIterator.hasNext()){
                if(usernameIterator.next().equals(userName))
                    return false;
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
