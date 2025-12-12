package family_tree.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @PostConstruct
    public void init() {
        LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("[%d{yyyy-MM-dd HH:mm:ss}] %-5level %logger{36} - %msg%n");
        encoder.start();

        FileAppender fileAppender = new FileAppender();
        fileAppender.setContext(context);
        fileAppender.setName("FILE");
        fileAppender.setFile("logs/application.log");
        fileAppender.setEncoder(encoder);
        fileAppender.start();

        Logger rootLogger = context.getLogger("ROOT");
        rootLogger.detachAndStopAllAppenders();
        rootLogger.addAppender(fileAppender);

        rootLogger.setLevel(Level.INFO);
    }
}
