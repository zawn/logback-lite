package com.appunity.logger;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ch.qos.logback.classic.LoggerContext;

public class LoggerTest {
    private static final String srcFile = "D:\\GitHub\\android-contexts\\logger\\build\\dump.txt";

    @Test
    public void largeAndDaysTest() throws IOException, InterruptedException {
        int day = 10;
        while (day < 11) {
            for (int i = 0; i < 1; i++) {
                testLogger(day);
                Thread.sleep(500);
            }
            day++;
        }


    }

    @Test
    public void testKey() {
        String keyString = "liFkRAy9Vqg=";
        byte[] keyBytes = Base64.decodeBase64(keyString);
        System.out.println(HexDump.dumpHexString(keyBytes));
    }

    private static SecretKey getKey() {
        String keyString = "liFkRAy9Vqg=";
        byte[] keyBytes = Base64.decodeBase64(keyString);
        return new SecretKeySpec(keyBytes, "DES");
    }

    private void testLogger(int day) throws IOException {

        System.out.println(new Date() + "  " + day);
        LoggerConfig.configure("D:/testlog", getKey());
        final Logger logger = LoggerFactory.getLogger(ExampleUnitTest.class);

        File file3 = new File(srcFile);
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file3));
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            logger.debug(line);
            line = bufferedReader.readLine();
        }
        LoggerContext loggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerFactory.stop();
    }

    @Test
    public void date() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHssmm");
        String beforeS = TimeMachine.now().format(dateTimeFormatter);
        System.out.println("beforeS " + beforeS);

        LocalDateTime fixTime = LocalDateTime.parse("20081212085959", dateTimeFormatter);
        TimeMachine.useFixedClockAt(fixTime);

        String afterS = TimeMachine.now().format(dateTimeFormatter);
        System.out.println("afterS " + afterS);

    }

    public static class TimeMachine {

        private static Clock clock = Clock.systemDefaultZone();
        private static ZoneId zoneId = ZoneId.systemDefault();

        public static LocalDateTime now() {
            return LocalDateTime.now(getClock());
        }

        public static void useFixedClockAt(LocalDateTime date) {
            clock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
        }

        public static void useSystemDefaultZoneClock() {
            clock = Clock.systemDefaultZone();
        }

        private static Clock getClock() {
            return clock;
        }
    }
}
