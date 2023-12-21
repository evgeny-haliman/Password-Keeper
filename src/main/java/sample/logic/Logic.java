package sample.logic;

import org.jasypt.util.text.AES256TextEncryptor;

public class Logic extends ErrorText {
    private static Logic instance;
    private final AES256TextEncryptor aesEncryptor;

    private Logic(String mainPassword) {
        aesEncryptor = new AES256TextEncryptor();
        aesEncryptor.setPassword(mainPassword);
    }

    public static synchronized Logic getInstance(String password) {
        if (instance == null) {
            instance = new Logic(password);
        }
        return instance;
    }

    public String encrypt(String valueToEnc) {
        return aesEncryptor.encrypt(valueToEnc);
    }

    public String decrypt(String encryptedValue) {
        return aesEncryptor.decrypt(encryptedValue);
    }
}
