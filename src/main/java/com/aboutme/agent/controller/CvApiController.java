package com.aboutme.agent.controller;

import com.aboutme.agent.services.CvPdfService;
import com.example.api.CvApi;
import com.example.model.CvRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CvApiController implements CvApi {

    private final CvPdfService pdfService = new CvPdfService();

    @Override
    public ResponseEntity<Resource> generateCv(CvRequest request) {

        byte[] pdf = pdfService.generatePdf(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cv.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdf));
    }
}
