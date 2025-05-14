package com.redmopag.documentmanagment.documentservice.utils;

import com.redmopag.documentmanagment.documentservice.exception.badrequest.InvalidFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class DocumentValidator {
    public void validateFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new InvalidFileException("Необходимо загрузить хотя бы один файл.");
        }
        boolean allImages = isAllImages(files);
        boolean isSinglePdf = isSinglePdf(files);
        if (!allImages && !isSinglePdf) {
            throw new InvalidFileException("Разрешается загрузка либо одного PDF-файла, либо нескольких изображений.");
        }
        //TODO: поискать другой способ проверки размера файла
        if (isSinglePdf) {
            if (files.get(0).getSize() > 1024 * 1024) {
                throw new InvalidFileException("Размер файла не должен превышать 10 мб");
            }
        }
        if (allImages) {
            var sum = files.stream().map(MultipartFile::getSize).reduce(0L, Long::sum);
            if (sum > 1024 * 1024) {
                throw new InvalidFileException("Общий размер изображений не должен превышать 10 мб");
            }
        }
    }

    private boolean isAllImages(List<MultipartFile> files) {
        return files.stream().allMatch(f ->
                f.getContentType() != null && f.getContentType().startsWith("image/"));
    }

    private boolean isSinglePdf(List<MultipartFile> files) {
        return files.size() == 1 &&
                files.get(0).getContentType() != null &&
                files.get(0).getContentType().equals("application/pdf");
    }
}
