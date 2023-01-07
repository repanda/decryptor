import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.repanda.decryptor.service.FileData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.repanda.decryptor.service.DecryptorService.decryptedFileData;

public class DecryptTest {


    @Test
    public void decryptFile() throws IOException {
        FileData fileData = decryptedFileData(getArgs());
        Assertions.assertTrue(fileData.getOutputFilePath().endsWith("file"));

    }

    private static String[] getArgs() {
        List<String> list = new ArrayList<>();
        list.add("lib/myKey.bckey"); // exported key
        list.add("src/test/resources/file.bc"); // encrypted file
        list.add("password"); // password
        return list.toArray(new String[3]);
    }

}
