package com.appunity.logger;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";
    private static final boolean DEBUG = true;

    @Test
    public void useAppContext() {
        Log.d(TAG, "useAppContext: ");
        System.out.println("ExampleInstrumentedTest.useAppContext");
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.appunity.logger", appContext.getPackageName());

        SecretKey keyAndroid = getKeyAndroid();
        // 0x00000000 96 21 64 44 0C BD 56 A8                         .!dD..V.
        System.out.println(HexDump.dumpHexString(keyAndroid.getEncoded()));
    }

    private static SecretKey getKeyAndroid() {
        String keyString = "liFkRAy9Vqg=";
        byte[] keyBytes = android.util.Base64.decode(keyString, android.util.Base64.DEFAULT);
        Log.d(TAG, "getKeyAndroid: " + HexDump.dumpHexString(keyBytes));
        return new SecretKeySpec(keyBytes, "DES");
    }
}
