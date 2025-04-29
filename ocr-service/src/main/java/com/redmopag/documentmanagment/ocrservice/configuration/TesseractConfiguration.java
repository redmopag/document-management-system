package com.redmopag.documentmanagment.ocrservice.configuration;

import net.sourceforge.tess4j.*;
import org.springframework.context.annotation.*;

import java.util.Collections;

@Configuration
public class TesseractConfiguration {
    @Bean
    public ITesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tesseract/");
        tesseract.setLanguage("rus");
        tesseract.setPageSegMode(1);
        tesseract.setConfigs(Collections.singletonList("hocr"));
        return tesseract;
    }
}
