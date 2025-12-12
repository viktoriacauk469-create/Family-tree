package family_tree.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class BusinessLogger {
    public void logInfo(String status, String method, Object[] args, Object result) {
        log.info("{} | Method: {} | Args: {} | Result: {}", status, method, Arrays.toString(args), result);
    }

    public void logError(String method, Object[] args, Throwable exception) {
        log.error("ERROR | Method: {} | Args: {} | Exception: {}", method, Arrays.toString(args), exception.getMessage(), exception);
    }
}