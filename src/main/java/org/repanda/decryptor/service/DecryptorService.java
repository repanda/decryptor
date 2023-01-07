package org.repanda.decryptor.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class DecryptorService {

    public static FileData decryptedFileData(String[] args) throws IOException {
        System.out.println("Decryption process started");

        // ============================================
        // AES decryption of private key in .bckey file
        // =============================================

        // collect information about the user account
        AccountData accountInfo = new AccountData();
        accountInfo.parseBCKeyFile(args[0]);
        accountInfo.setPassword(args[2]);

        // decrypt the private key from the .bckey file
        byte[] decryptedPrivateKey = AESHelper.decryptDataPBKDF2(
                accountInfo.getEncryptedPrivateKey(), accountInfo.getPassword(),
                accountInfo.getPBKDF2Salt(), accountInfo.getPBKDF2Iterations()
        );

        // =============================================
        // RSA decryption of file information (header)
        // =============================================

        // collect information about the file to be decrypted
        FileData fileData = new FileData();
        String outputFilePath = args.length > 3 ? args[3] : "";
        fileData.parseHeader(args[1], outputFilePath);

        // decrypt the file key (from file header) used for decryption of file data
        byte[] decryptedFileKey = RSAHelper.decryptData(fileData.getEncryptedFileKey(), decryptedPrivateKey);

        byte[] fileCryptoKey = Arrays.copyOfRange(decryptedFileKey, 32, 64);
        // =============================================
        // AES decryption of encrypted file
        // =============================================

        // decrypt the file data ...
        byte[] decryptedFileBytes = AESHelper.decryptFile(
                fileData.getEncryptedFilePath(), fileCryptoKey, fileData.getBaseIVec(),
                fileData.getBlockSize(), fileData.getHeaderLen(), fileData.getCipherPadding()
        );

        Files.write(Paths.get(fileData.getOutputFilePath()), decryptedFileBytes);
        return fileData;
    }
}
