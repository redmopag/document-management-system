package com.redmopag.documentmanagment.ocrservice.service;

import com.redmopag.documentmanagment.ocrservice.utils.FileConverter;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class PreprocessServiceImpl {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public File preprocess(File file) {
        Mat preprocessImage = preprocessImage(file);
        return FileConverter.convertToFile(preprocessImage);
    }

    private Mat preprocessImage(File file) {
        Mat image = Imgcodecs.imread(file.getAbsolutePath());
        var gray = convertToGray(image);
        var blurred = blur(gray);
        return binarize(blurred);
    }

    private Mat blur(Mat gray) {
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(gray, blurred, new Size(3, 3), 0);
        return blurred;
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

    private Mat binarize(Mat prev) {
        Mat thresh = new Mat();
        Imgproc.adaptiveThreshold(prev, thresh, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 15, 10);
        return thresh;
    }
}
