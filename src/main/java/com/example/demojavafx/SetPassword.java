package com.example.demojavafx;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;

public class SetPassword {

    public static void PDF(File f,String password) throws IOException {
        PDDocument doc = PDDocument.load(f);
        int keyLength = 128;
        AccessPermission ap = new AccessPermission();
        ap.setCanPrint(false);
        StandardProtectionPolicy spp = new StandardProtectionPolicy(password,password,ap);
        spp.setEncryptionKeyLength(keyLength);
        doc.protect(spp);

        doc.save(f);
        doc.close();
    }
}
