package com.redmopag.documentmanagment.ocrservice.client;

import com.redmopag.documentmanagment.ocrservice.exception.InvalidFileException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

@Component
public class FileDownloader {
    /* Use the JDK HttpURLConnection (since v1.1) class to do the download. */
    public File downloadFile(String presignedUrlString, String filePostfix) {
        try {
            URL presignedUrl = new URL(presignedUrlString);
            HttpURLConnection connection = (HttpURLConnection) presignedUrl.openConnection();
            connection.setRequestMethod("GET");
            try (InputStream inputStream = connection.getInputStream()) {
                File tempFile = File.createTempFile("download-", "." + filePostfix);
                try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    inputStream.transferTo(outputStream);
                }
                return tempFile;
            }
        } catch (IOException e) {
            throw new InvalidFileException(e.getMessage());
        }
    }
}
