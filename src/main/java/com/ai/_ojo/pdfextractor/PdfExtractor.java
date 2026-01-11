package com.ai._ojo.pdfextractor;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.IOException;

public class PdfExtractor {
    public static String extractText(byte[] bytes) throws IOException {
        // Loader.loadPDF es el estándar en 2026
        try (PDDocument document = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}

