package Utils;

import java.util.HashMap;
import java.util.Map;

public class SecurityQuestion {
    private Map<Integer, String> securityQuestions = new HashMap<>();

    public SecurityQuestion() {
        securityQuestions.put(1, "What is your mother's maiden name?");
        securityQuestions.put(2, "What was the name of your first pet?");
        securityQuestions.put(3, "What was the name of your elementary school?");
    }

    public String getQuestion(int id) {
        return securityQuestions.get(id);
    }
}
