package Model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private String password;
    private Map<Integer, String> securityAnswers = new HashMap<>();

    public User() {

    }

    public User(String userId, String password, Map<Integer, String> securityAnswers) {
        this.userId = userId;
        this.password = password;
        this.securityAnswers = securityAnswers;
    }

    public void setUserId(String id){
        this.userId = id;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setSecurityAnswers(Map<Integer,String> answers){
        this.securityAnswers = answers;
    }
    public String getPassword() {
        return password;
    }
    public String getUserId(){
        return userId;
    }
    public String getSecurityAnswer(int id){
        return securityAnswers.get(id);
    }
    public Map<Integer,String> getSecurityAnswers(){
        return securityAnswers;
    }
}
