package org.example.miniprojet.controller;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.miniprojet.models.Patient;

import java.io.File;
import java.io.IOException;

public class PDFCreator {

    public static void createPDF(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            try {
                // Initialize PDF writer
                PdfWriter writer = new PdfWriter(filePath);

                // Initialize PDF document
                PdfDocument pdf = new PdfDocument(writer);

                // Initialize document
                Document document = new Document(pdf, PageSize.A4);

                // Add content to the PDF
                PdfFont font = PdfFontFactory.createFont();
//                Paragraph paragraph = new Paragraph("Hello, this is a PDF file created using iText library!");
//                document.add(paragraph);

                // Add an image to the PDF
                String imagePath = "C:/Users/Alpha/Desktop/logo-red.png";
                Image image = new Image(ImageDataFactory.create(imagePath));
                document.add(image);

                // Close the document
                document.close();

                System.out.println("PDF created successfully at: " + filePath);
            } catch (IOException e) {
                System.err.println("Error creating PDF: " + e.getMessage());
            }
        }
    }



    public static void createPDF(Stage stage, Patient patient) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            try {
                // Initialize PDF writer
                PdfWriter writer = new PdfWriter(filePath);

                // Initialize PDF document
                PdfDocument pdf = new PdfDocument(writer);

                // Initialize document
                Document document = new Document(pdf, PageSize.A4);

                // Add patient information to the PDF
                Paragraph patientInfo = new Paragraph("Patient Information:\n" +
                        "CIN: " + patient.getCin() + "\n" +
                        "Name: " + patient.getNom() + "\n" +
                        "Surname: " + patient.getPrenom() + "\n" +
                        "Phone: " + patient.getTel() + "\n" +
                        "Sex: " + patient.getSexe() + "\n"
                );
                document.add(patientInfo);

                // Add content to the PDF
                PdfFont font = PdfFontFactory.createFont();
//                Paragraph paragraph = new Paragraph("Hello, this is a PDF file created using iText library!");
//                document.add(paragraph);

                // Add an image to the PDF
                String imagePath = "C:/Users/Alpha/Desktop/logo-red.png";
                Image image = new Image(ImageDataFactory.create(imagePath));
                document.add(image);

                // Close the document
                document.close();

                System.out.println("PDF created successfully at: " + filePath);
            } catch (IOException e) {
                System.err.println("Error creating PDF: " + e.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        createPDF(new Stage());
    }
}

