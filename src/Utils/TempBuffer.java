package Utils;
import Model.User;

import java.util.*;

public class TempBuffer {

    Map<String,User> usersMap = new HashMap<>();
    IOHandler io;

    public TempBuffer(){
        io = new IOHandler();
    }

    public Map<String,User> LoadUserProfileData(){
        this.usersMap = io.ReadUserDataFromFile();
        return usersMap;
    }

}
