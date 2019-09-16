package com.appunity.logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String DES_ECB_NO_PADDING = CipherRollingFileAppender.DES_ECB_NO_PADDING;
    private static final int KEY_SIZE = 8;

    public static SecretKey getKey(String base64Key) {
        byte[] keyBytes = Base64.decodeBase64(base64Key);
        return new SecretKeySpec(keyBytes, "DES");
    }

    private SecretKey getKey() {
        return getKey("liFkRAy9Vqg=");
    }

    public static void configure(String logDir) {

        // assume SLF4J is bound to logback in the current environment
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

        RollingFileAppender<ILoggingEvent> fileAppender = LoggerConfig.createFileAppender(loggerContext, logDir);
        rootLogger.addAppender(fileAppender);
    }

    @Test
    public void test1() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        final Logger logger = LoggerFactory.getLogger(ExampleUnitTest.class);

        File file = new File("D:/logtest/log.txt");
        file.delete();
        file = new File("D:/logtest/log_en.txt");
        file.delete();
        file = new File("D:/logtest/log_raw.txt");
        file.delete();


        LoggerConfig.configure("D:/logtest", getKey());
        configure("D:/logtest");

        assertEquals(4, 2 + 2);

        File file3 = new File("E:\\log_test.txt");
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file3));
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            logger.debug(line);
            line = bufferedReader.readLine();
        }
//        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//        context.stop();
//
//        encrypt2();
    }


    @Test
    public void addition_isCorrect() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        final Logger logger = LoggerFactory.getLogger(ExampleUnitTest.class);
//        File file = new File("D:/logtest/log.bin");
//        file.delete();
//        file = new File("D:/logtest/log_en.txt");
//        file.delete();
//        file = new File("D:/logtest/log_raw.txt");
//        file.delete();

        LoggerConfig.configure("D:/logtest", getKey());
//        logger.debug("123456789012345");
        logger.debug("1");
//        logger.debug("3");
//        logger.debug("4");
//        logger.debug("5");
//        logger.debug("addition_isCorrect() called 4");
//        logger.debug("addition_isCorrect() called 5");
//        logger.debug("addition_isCorrect() called 6");
//        logger.debug("addition_isCorrect() called 7");
//        logger.debug("addition_isCorrect() called 8");
        assertEquals(4, 2 + 2);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.stop();

        encrypt2();
    }

    @Test
    public void decrypt2() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        File logFile = new File("D:/logtest/log.bin");
        if (logFile.exists()) {
            String password = "liFkRAy9Vqg=";
            byte[] keyBytes = Base64.decodeBase64(password);
            System.out.println(HexDump.dumpHexString(keyBytes));
            final Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "DES"));
            CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(logFile), cipher);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:/logtest/log.dec.log"))));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cipherInputStream));
            String line = bufferedReader.readLine();
            while (line != null) {
                byte[] bytes = line.getBytes();
                int i = 0;
                int c = bytes[i];
                while (c == 0 && i < bytes.length - 1) {
                    i++;
                    c = bytes[i];
                }
                line = new String(bytes, i, bytes.length - i);
                bufferedWriter.write(line + "\r\n");
                line = bufferedReader.readLine();
            }
            cipherInputStream.close();
            bufferedWriter.flush();
            bufferedWriter.close();
        } else {
            System.out.println("file not exist!");
        }
    }

    @Test
    public void decryptBinLogToFile() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        File logFile = new File("D:/logtest/log.bin");
        if (logFile.exists()) {
            String password = "liFkRAy9Vqg=";
            byte[] keyBytes = Base64.decodeBase64(password);
            System.out.println(HexDump.dumpHexString(keyBytes));
            String desEcbNoPadding = DES_ECB_NO_PADDING;
//            desEcbNoPadding = "DES/ECB/NoPadding";
            final Cipher cipher = Cipher.getInstance(desEcbNoPadding);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "DES"));
            CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(logFile), cipher);
            FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/logtest/log.dec.log"));
            IOUtils.copy(cipherInputStream, fileOutputStream);
            cipherInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } else {
            System.out.println("file not exist!");
        }
    }

    @Test
    public void encryptDES() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        File logFile = new File("D:/logtest/log_test.txt");
        if (logFile.exists()) {
            logFile.delete();
            logFile.createNewFile();
        } else {
            logFile.createNewFile();
        }
        String password = "liFkRAy9Vqg=";
        byte[] keyBytes = Base64.decodeBase64(password);
        System.out.println(HexDump.dumpHexString(keyBytes));

        final String transformation = "DES/ECB/NoPadding";
        final Cipher cipher = Cipher.getInstance(transformation);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        OutputStream os = new FileOutputStream(logFile);
        os = new CipherOutputStream(os, cipher);
//            BufferedWriter writer
//                    = new BufferedWriter(new OutputStreamWriter(os));
////            writer.write("zhang");
//            writer.write('1');
//            writer.write('1');
//            writer.write('1');
//            writer.write('1');
//            writer.write('1');
//            writer.write('1');
//            writer.flush();
//            writer.close();
        final byte[] ivBytes2 = new byte[]{
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07
        };
        os.write(ivBytes2);
        os.close();
    }

    @Test
    public void encrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        File logFile = new File("D:/logtest/log_test.txt");
        if (logFile.exists()) {
            logFile.delete();
            logFile.createNewFile();
        } else {
            logFile.createNewFile();
        }
        String password = "liFkRAy9Vqg=";
        byte[] keyBytes = Base64.decodeBase64(password);
        System.out.println(HexDump.dumpHexString(keyBytes));

        final String transformation = "AES/ECB/NoPadding";
//        final byte[] ivBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
//                0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f}; //example

//        final IvParameterSpec IV = new IvParameterSpec(ivBytes);
        final Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, genKey());
        OutputStream os = new FileOutputStream(logFile);
        os = new CipherOutputStream(os, cipher);
//            BufferedWriter writer
//                    = new BufferedWriter(new OutputStreamWriter(os));
////            writer.write("zhang");
//            writer.write('1');
//            writer.write('1');
//            writer.write('1');
//            writer.write('1');
//            writer.write('1');
//            writer.write('1');
//            writer.flush();
//            writer.close();
        final byte[] ivBytes2 = new byte[]{
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06
        };
        os.write(ivBytes2);
        os.close();
    }


    @Test
    public void encrypt2() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        File logFile = new File("D:\\logtest\\log_en.txt");
        File raw = new File("D:/logtest/log_raw.txt");
        if (logFile.exists()) {
            logFile.delete();
        }
        String password = "liFkRAy9Vqg=";
        byte[] keyBytes = Base64.decodeBase64(password);
        System.out.println(HexDump.dumpHexString(keyBytes));
        final Cipher cipher = Cipher.getInstance(DES_ECB_NO_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "DES"));
        OutputStream os = new FileOutputStream(logFile);
        os = new CipherOutputStream(os, cipher);
        FileInputStream fileInputStream = new FileInputStream(raw);
        IOUtils.copy(fileInputStream, os);
        fileInputStream.close();
        os.flush();
        os.close();
    }

    @Test
    public void generateKey() {
        System.out.println(deriveKeySecurely("zhang", 8));
        System.out.println(deriveKeySecurely("zhang", 8));
        System.out.println(deriveKeySecurely("zhang", 16));
        System.out.println(deriveKeySecurely("zhang", 16));
    }


    /**
     * Example use of a key derivation function, derivating a key securely from a password.
     */
    private String deriveKeySecurely(String password, int keySizeInBytes) {
        // Use this to derive the key from the password:
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), retrieveSalt(),
                100 /* iterationCount */, keySizeInBytes * 8 /* key size in bits */);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            return Base64.encodeBase64String(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException("Deal with exceptions properly!", e);
        }
    }

    private byte[] retrieveSalt() {
        // Salt must be at least the same size as the key.
        byte[] salt = new byte[KEY_SIZE];
        // Create a random salt if encrypting for the first time, and save it for future use.
        readFromFileOrCreateRandom("salt", salt);
        System.out.println("ExampleUnitTest.retrieveSalt"+ HexDump.dumpHexString(salt));
        return salt;
    }

    /**
     * Read from file or return random bytes in the given array.
     *
     * <p>Save to file if file didn't exist.
     */
    private void readFromFileOrCreateRandom(String fileName, byte[] bytes) {
//        if (fileExists(fileName)) {
//            readBytesFromFile(fileName, bytes);
//            return;
//        }
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(bytes);
//        writeToFile(fileName, bytes);
    }

    private static SecretKey genKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        /* User types in their password: */
        String password = "password";

        /* Store these things on disk used to derive key later: */
        int iterationCount = 1000;
        int saltLength = 32; // bytes; should be the same size as the output (256 / 8 = 32)
        int keyLength = 128; // 256-bits for AES-256, 128-bits for AES-128, etc byte[] salt; // Should be of saltLength

        /* When first creating the key, obtain a salt with this: */
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);

        String string = Base64.encodeBase64String(salt);
        System.out.println("Salt :" + string);
        System.out.println(HexDump.dumpHexString(salt));
        /* Use this to derive the key from the password: */
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        String keyString = Base64.encodeBase64String(keyBytes);
        System.out.println(keyString);
        System.out.println(HexDump.dumpHexString(keyBytes));
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }

}