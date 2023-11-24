package jmouseable.jmouseable;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class JmouseableApplication {

    private static final Logger logger =
            (Logger) LoggerFactory.getLogger(JmouseableApplication.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        Stream.of(args)
              .filter(arg -> arg.startsWith("--log-level="))
              .map(arg -> arg.split("=")[1])
              .findFirst()
              .ifPresent(JmouseableApplication::setLogLevel);
        Path configurationPath = Stream.of(args)
                                       .filter(arg -> arg.startsWith(
                                               "--configuration-file="))
                                       .map(arg -> arg.split("=")[1])
                                       .findFirst()
                                       .map(Paths::get)
                                       .orElse(Paths.get("jmouseable.properties"));
        if (Stream.of(args).anyMatch(Predicate.isEqual(("--graalvm-agent-run")))) {
            logger.info("--graalvm-agent-run flag found, exiting in 5s");
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            }).start();
        }
        new Jmouseable(configurationPath, new WindowsManager()).run();
    }

    private static void setLogLevel(String level) {
       Logger logger = (Logger) LoggerFactory.getLogger("jmouseable");
       logger.setLevel(Level.valueOf(level));
    }

}
