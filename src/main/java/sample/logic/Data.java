package sample.logic;

import static java.lang.String.join;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Data extends ErrorText {
    private static final String PASS_FOLDER = Paths.get("src/main/resources").toString();
    File file;

    public Data() {
        try {
            file = new File(PASS_FOLDER.concat("/data.txt"));
            log.info("was data file created? : " + file.createNewFile());
        } catch (IOException ex) {
            logError(ex);
        }
    }

    public void writeLoginPassToData(String login, String password) {
        try {
            FileUtils.writeLines(file, List.of(join(":", login, password)), true);
        } catch (IOException ex) {
            logError(ex);
        }
    }

    public void deleteLoginPass(int index) {
        List<String> list = new ArrayList<>();
        try {
            list = FileUtils.readLines(file, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            logError(ex);
        }

        list.remove(index);

        try {
            FileUtils.writeLines(file, list, false);
        } catch (IOException ex) {
            logError(ex);
        }
    }

    public void updatePassword(int index, String password) {
        List<String> list = new ArrayList<>();
        try {
            list = FileUtils.readLines(file, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            logError(ex);
        }

        var lineByIndex = list.get(index);
        var updatedLine = lineByIndex.replaceFirst(":.+", ":".concat(password));
        list.set(index, updatedLine);

        try {
            FileUtils.writeLines(file, list, false);
        } catch (IOException ex) {
            logError(ex);
        }
    }

    public List<String> getEncryptedLogins() {
        List<String> loginPassList = new ArrayList<>();

        try {
            loginPassList = FileUtils.readLines(file, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            logError(ex);
        }

        return loginPassList.stream()
                .map(x -> x.split(":")[0]).toList();
    }

    public List<String> getEncryptedPasses() {
        List<String> loginPassList = new ArrayList<>();

        try {
            loginPassList = FileUtils.readLines(file, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            logError(ex);
        }

        return loginPassList.stream()
                .map(x -> x.split(":")[1]).toList();
    }
}
