package com.appunity.logger;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * 使用简单的DES对称加密算法进行日志流进行加密输出.
 * <br/>加密算法:
 * <br/>&nbsp;&nbsp;DES/ECB/NoPadding
 * <br/>填充方式如PKCS5Padding并不适合日志输出，且可能导致输出意外终端后无法解码
 * 或者数据丢失
 *
 * @param <E>
 * @author zhangzhenli
 */
public class CipherRollingFileAppender<E> extends RollingFileAppender<E> {

    /**
     * 使用ECB/NoPadding 单个错误仅对一个密文块产生影响.
     */
    public static final String DES_ECB_NO_PADDING = "DES/ECB/NoPadding";
    private final SecretKey secretKey;

    public CipherRollingFileAppender(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 使用0 填充分块不足的字节,防止分块不足字节被丢弃.
     * <br/>该填充方式非通用填充,会导致加解密后字节不一致。
     */
    private static class PaddingOutputStream extends FilterOutputStream {

        private final long size;
        protected long count;

        public PaddingOutputStream(OutputStream out, int size) {
            super(out);
            this.size = size;
            this.count = 0;
        }

        @Override
        public void write(int b) throws IOException {
            count++;
            super.write(b);
        }

        @Override
        public synchronized void flush() throws IOException {
            long i = count % size;
            while (i != 0 && i < size) {
                super.write(0);
                i++;
            }
            count = 0;
            super.flush();
        }
    }

    @Override
    public void setOutputStream(OutputStream outputStream) {
        lock.lock();
        try {
            // close any previously opened output stream
            closeOutputStream();
            PaddingOutputStream bufferedOutputStream = null;

            try {
                final Cipher cipher = Cipher.getInstance(DES_ECB_NO_PADDING);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
                bufferedOutputStream = new PaddingOutputStream(cipherOutputStream, 8);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }
            if (bufferedOutputStream == null) {
                // 加密流创建失败,回退
                super.setOutputStream(outputStream);
            } else {
                super.setOutputStream(bufferedOutputStream);
            }
        } finally {
            lock.unlock();
        }
    }
}
