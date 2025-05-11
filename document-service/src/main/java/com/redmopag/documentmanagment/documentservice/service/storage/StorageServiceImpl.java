package com.redmopag.documentmanagment.documentservice.service.storage;

import com.redmopag.documentmanagment.common.GenerateLinkResponse;
import com.redmopag.documentmanagment.documentservice.client.FileStorageClient;
import com.redmopag.documentmanagment.documentservice.exception.badrequest.InvalidFileException;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {
    private final FileStorageClient fileStorageClient;

    public StorageServiceImpl(FileStorageClient fileStorageClient) {
        this.fileStorageClient = fileStorageClient;
    }

    @Override
    public void upload(Long fileId, List<MultipartFile> files) {
        try {
            ByteArrayResource fileAsResource = getFileAsResource(files);
            fileStorageClient.sendFile(fileId, fileAsResource).subscribe();
        } catch (IOException e) {
            throw new InvalidFileException(e.getMessage());
        }
    }

    private ByteArrayResource getFileAsResource(List<MultipartFile> files) throws IOException {
        byte[] outputStream;
        String fileName;
        if (isImage(files)) {
            outputStream = convertToPdfStream(files).toByteArray();
            fileName = files.get(0).getOriginalFilename().split("\\.")[0] + ".pdf";
        } else {
            outputStream = files.get(0).getBytes();
            fileName = files.get(0).getOriginalFilename();
        }
        return convertToByteArrayResource(
                outputStream,
                fileName);
    }

    private boolean isImage(List<MultipartFile> files) {
        return files.get(0).getOriginalFilename().startsWith("image");
    }

    private ByteArrayResource convertToByteArrayResource(byte[] byteOutputStream, String fileName) {
        return new ByteArrayResource(byteOutputStream) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };
    }

    public ByteArrayOutputStream convertToPdfStream(List<MultipartFile> files) throws IOException {
        PDDocument document = convertJpgsToPdf(files);
        return saveToOutputStream(document);
    }

    private PDDocument convertJpgsToPdf(List<MultipartFile> files) throws IOException {
        PDDocument document = new PDDocument();
        for (MultipartFile file : files) {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            PDPage page = addPageToDocument(bufferedImage, document);
            drawImageOnPage(document, page, bufferedImage);
        }
        return document;
    }

    private ByteArrayOutputStream saveToOutputStream(PDDocument document) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        return outputStream;
    }

    private PDPage addPageToDocument(BufferedImage bufferedImage, PDDocument document) {
        PDPage page = new PDPage(new PDRectangle(bufferedImage.getWidth(), bufferedImage.getHeight()));
        document.addPage(page);
        return page;
    }

    private void drawImageOnPage(PDDocument document, PDPage page, BufferedImage bufferedImage) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
        contentStream.drawImage(pdImage, 0, 0);
        contentStream.close();
    }

    @Override
    public String getDownloadUrl(String objectKey) {
        GenerateLinkResponse response = fileStorageClient.generatePresignedUrl(objectKey);
        return response.getDownloadUrl();
    }
}
