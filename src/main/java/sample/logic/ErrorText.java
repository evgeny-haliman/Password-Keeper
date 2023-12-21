package sample.logic;

import static java.lang.Thread.currentThread;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ErrorText {
    void logError(Exception ex) {
        log.error(ex + " in " + this.getClass()
                .getName() + " at line " + currentThread().getStackTrace()[2].getLineNumber());
    }
}
