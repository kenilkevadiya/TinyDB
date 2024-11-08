package Utils;
import Constants.Constants;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionAlgorithm {
    MessageDigest md;
    public EncryptionAlgorithm(){}

    public String hashData(String data) {
        try {
            md =  MessageDigest.getInstance(Constants.ALGORITHMINSTANCE);
            md.update(data.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
