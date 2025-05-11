package com.redmopag.documentmanagment.textservice.controller;

import com.redmopag.documentmanagment.common.TextResponse;
import com.redmopag.documentmanagment.textservice.service.IndexService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TextController {
    private final IndexService indexService;

    public TextController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping("/text/search")
    @ResponseStatus(HttpStatus.OK)
    public List<TextResponse> search(@RequestParam("text") String text) {
        return indexService.search(text);
    }

    @GetMapping("/text")
    @ResponseStatus(HttpStatus.OK)
    public TextResponse getTextById(@RequestParam("id") String id) {
        return indexService.getDocumentTextById(id);
    }
}
