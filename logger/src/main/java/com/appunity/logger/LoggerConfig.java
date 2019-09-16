/**
 * Copyright 2019 Anthony Trinh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appunity.logger;

import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

/**
 * LoggerConfig configures logback-classic by attaching a
 * {@link LogcatAppender} to the root logger. The appender's layout is set to a
 * {@link ch.qos.logback.classic.PatternLayout} with the pattern "%msg".
 * <p>
 * The equivalent default configuration in XML would be:
 * <pre>
 * &lt;configuration&gt;
 *  &lt;appender name="LOGCAT"
 *           class="ch.qos.logback.classic.android.LogcatAppender" &gt;
 *      &lt;checkLoggable&gt;false&lt;/checkLoggable&gt;
 *      &lt;encoder&gt;
 *          &lt;pattern&gt;%msg&lt;/pattern&gt;
 *      &lt;/encoder&gt;
 *  &lt;/appender&gt;
 *  &lt;root level="DEBUG" &gt;
 *     &lt;appender-ref ref="LOGCAT" /&gt;
 *  &lt;/root&gt;
 * &lt;/configuration&gt;
 * </pre>
 *
 * @author zhangzhenli
 */
public class LoggerConfig {

    public static final String PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p --- [%t] %-40.40logger{39}- %msg%n";
    //    public static final String PATTERN = "%msg";

    public static void configure(String logDir, SecretKey secretKey) {

        // assume SLF4J is bound to logback in the current environment
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

        if (isDalvikVm()) {
            loggerContext.reset();
            LogcatAppender logcatAppender = createLogcatAppender(loggerContext);
            rootLogger.addAppender(logcatAppender);
        }

        RollingFileAppender<ILoggingEvent> rollingFileAppender = createCipherFileAppender(loggerContext, logDir, secretKey);
        rootLogger.addAppender(rollingFileAppender);
    }

    private static boolean isDalvikVm() {
        return "Dalvik".equals(System.getProperty("java.vm.name"))
                || System.getProperty("java.vendor").contains("Android");
    }

    public static LogcatAppender createLogcatAppender(LoggerContext lc) {
        LogcatAppender logcat = new LogcatAppender();
        logcat.setContext(lc);
        logcat.setName("logcat");

        // We don't need a trailing new-line character in the pattern
        // because logcat automatically appends one for us.
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        encoder.setPattern("[%thread] %msg%n");
        encoder.start();

        logcat.setEncoder(encoder);
        PatternLayoutEncoder tagEncoder = new PatternLayoutEncoder();
        tagEncoder.setContext(lc);
        tagEncoder.setPattern("%class{0}");
        tagEncoder.start();
        logcat.setTagEncoder(tagEncoder);
        logcat.start();
        return logcat;
    }

    public static RollingFileAppender<ILoggingEvent> createCipherFileAppender(LoggerContext context, String logDir, SecretKey secretKey) {

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new CipherRollingFileAppender<>(secretKey);
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setName("CipherAppender");
        rollingFileAppender.setContext(context);

        // OPTIONAL: Set an active log file (separate from the rollover files).
        // If rollingPolicy.fileNamePattern already set, you don't need this.
        rollingFileAppender.setFile(logDir + "/log.bin");

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
        rollingPolicy.setFileNamePattern(logDir + "/log.%d.bin");
        rollingPolicy.setMaxHistory(7);
        rollingPolicy.setParent(rollingFileAppender);  // parent and context required!
        rollingPolicy.setContext(context);
        rollingPolicy.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern(PATTERN);
        encoder.setContext(context);
        encoder.start();

        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();
        return rollingFileAppender;
    }

    public static RollingFileAppender<ILoggingEvent> createFileAppender(LoggerContext context, String logDir) {

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setName("RollingFileAppender");
        rollingFileAppender.setContext(context);

        // OPTIONAL: Set an active log file (separate from the rollover files).
        // If rollingPolicy.fileNamePattern already set, you don't need this.
        rollingFileAppender.setFile(logDir + "/log.txt");

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
        rollingPolicy.setFileNamePattern(logDir + "/log.%d.txt");
        rollingPolicy.setMaxHistory(7);
        rollingPolicy.setParent(rollingFileAppender);  // parent and context required!
        rollingPolicy.setContext(context);
        rollingPolicy.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern(PATTERN);
        encoder.setContext(context);
        encoder.start();

        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();
        return rollingFileAppender;
    }
}
