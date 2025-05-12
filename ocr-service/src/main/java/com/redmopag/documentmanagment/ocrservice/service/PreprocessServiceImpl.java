package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.ocrservice.client.FileDownloader;
import com.redmopag.documentmanagment.ocrservice.exception.OcrFailedException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class PreprocessServiceImpl {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private final FileDownloader fileDownloader;

    public PreprocessServiceImpl(FileDownloader fileDownloader) {
        this.fileDownloader = fileDownloader;
    }

    public File preprocess(String downloadUrl, String filePostfix) {
        var file = downloadFile(downloadUrl, filePostfix);
        Mat preprocessImage = preprocessImage(file);
        return convertToFile(preprocessImage, downloadUrl);
    }

    private File downloadFile(String downloadUrl, String filePostfix) {
        System.out.println("Скачивание файла по url: " + downloadUrl);
        File tempFile = fileDownloader.downloadFile(downloadUrl, filePostfix);
        System.out.println("Скачан файл: " + tempFile.getName());
        return tempFile;
    }

    private Mat preprocessImage(File file) {
        Mat image = Imgcodecs.imread(file.getAbsolutePath());
        increaseContrast(image);
        var gray = convertToGray(image);
        var diff = deleteBackground(gray);
        var norm = normalizeContrast(diff);
        return binarize(norm);
    }

    private void increaseContrast(Mat image) {
        Mat lab = new Mat();
        Imgproc.cvtColor(image, lab, Imgproc.COLOR_BGR2Lab);
        equalizeBrightness(lab);
        Imgproc.cvtColor(lab, image, Imgproc.COLOR_Lab2BGR);
    }

    private void equalizeBrightness(Mat lab) {
        List<Mat> labChannels = new ArrayList<>();
        Core.split(lab, labChannels);
        Imgproc.equalizeHist(labChannels.get(0), labChannels.get(0));
        Core.merge(labChannels, lab);
    }

    private Mat convertToGray(Mat image) {
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }

    private Mat deleteBackground(Mat gray) {
        Mat background = deleteBackgroundByMorph(gray);
        return subtractBackground(gray, background);
    }

    private Mat deleteBackgroundByMorph(Mat gray) {
        Mat morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15, 15));
        Mat background = new Mat();
        Imgproc.morphologyEx(gray, background, Imgproc.MORPH_CLOSE, morphKernel);
        return background;
    }

    private Mat subtractBackground(Mat gray, Mat background) {
        Mat diff = new Mat();
        Core.absdiff(gray, background, diff);
        return diff;
    }

    private Mat normalizeContrast(Mat diff) {
        Mat norm = new Mat();
        Core.normalize(diff, norm, 0, 255, Core.NORM_MINMAX);
        return norm;
    }

    private Mat binarize(Mat norm) {
        Mat thresh = new Mat();
        Imgproc.adaptiveThreshold(norm, thresh, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 15, 10);
        return thresh;
    }

    private File convertToFile(Mat preprocessImage, String downloadUrl) {
        try {
            File tempFile = File.createTempFile("preprocessed", ".png");
            Imgcodecs.imwrite(tempFile.getAbsolutePath(), preprocessImage);
            return tempFile;
        } catch (IOException e) {
            throw new OcrFailedException("Не удалось предобработать файл. Url: " + downloadUrl);
        }
    }
}
