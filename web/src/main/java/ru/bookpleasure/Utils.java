package ru.bookpleasure;

import java.security.MessageDigest;
import java.util.List;

/**
 * Created by Kirill on 29/07/16.
 */
public class Utils {

    public static String getHexString(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                sb.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                sb.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return sb.toString();
    }

    public static String sha1HashFromParams(List<String> strings) {
        try {
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < strings.size(); i++) {
                String string = strings.get(i);
                if (string != null) {
                    sb.append(string);
                }
                if (i < strings.size() - 1) {
                    sb.append("&");
                }
            }
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(sb.toString().getBytes());
            return getHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
