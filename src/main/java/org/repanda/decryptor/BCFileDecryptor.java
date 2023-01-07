package org.repanda.decryptor;

import org.repanda.decryptor.service.FileData;

import static org.repanda.decryptor.service.DecryptorService.decryptedFileData;

public class BCFileDecryptor {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: bc-file-decryptor "
                    + "[path to .bckey file] "
                    + "[path to encrypted file] "
                    + "[pwd] "
                    + "[path for output (optional)]"
            );

            return;
        }

        // for the sake of keeping this program short just catch
        // all exceptions in one place and show the error before exiting
        try {
            FileData fileData = decryptedFileData(args);

            System.out.println("Successfully decrypted file '" + fileData.getEncryptedFilePath() + "', "
                    + "output: '" + fileData.getOutputFilePath() + "'");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (e.getCause() != null) {
                System.err.println(e.getCause());
            }
        }
    }
}
