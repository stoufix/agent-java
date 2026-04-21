package com.aboutme.agent.services;


import com.example.model.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.ByteArrayOutputStream;

public class CvPdfService {

    public static byte[] generatePdf(CvRequest cv) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            addHeader(document, cv);
            addSummary(document, cv);
            addExperience(document, cv);
            addEducation(document, cv);
            addSkills(document, cv);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private static void addHeader(Document doc, CvRequest cv) {
        PersonalInfo p = cv.getPersonalInfo();

        Font nameFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font infoFont = new Font(Font.HELVETICA, 10);

        Paragraph name = new Paragraph(p.getFirstName() + " " + p.getLastName(), nameFont);
        name.setSpacingAfter(5);

        Paragraph contact = new Paragraph(
                p.getEmail() + " | " +
                        (p.getPhone() != null ? p.getPhone() : ""), infoFont
        );
        contact.setSpacingAfter(10);

        doc.add(name);
        doc.add(contact);
    }

    private static void addSummary(Document doc, CvRequest cv) throws Exception {
        if (cv.getSummary() == null) return;

        addSectionTitle(doc, "Profile");

        Paragraph summary = new Paragraph(cv.getSummary());
        summary.setSpacingAfter(10);

        doc.add(summary);
    }

    private static void addExperience(Document doc, CvRequest cv) throws Exception {
        if (cv.getExperience() == null) return;

        addSectionTitle(doc, "Experience");

        for (Experience exp : cv.getExperience()) {
            Paragraph title = new Paragraph(
                    exp.getPosition() + " - " + exp.getCompany(),
                    new Font(Font.HELVETICA, 12, Font.BOLD)
            );

            Paragraph dates = new Paragraph(
                    exp.getStartDate() + " - " +
                            (exp.getEndDate() != null ? exp.getEndDate() : "Present"),
                    new Font(Font.HELVETICA, 9, Font.ITALIC)
            );

            doc.add(title);
            doc.add(dates);

            if (exp.getResponsibilities() != null) {
                com.lowagie.text.List list = new com.lowagie.text.List(false, 10);
                for (String r : exp.getResponsibilities()) {
                    list.add(new ListItem(r));
                }
                doc.add(list);
            }

            doc.add(Chunk.NEWLINE);
        }
    }

    private static void addEducation(Document doc, CvRequest cv) throws Exception {
        if (cv.getEducation() == null) return;

        addSectionTitle(doc, "Education");

        for (Education edu : cv.getEducation()) {
            Paragraph p = new Paragraph(
                    edu.getDegree() + " - " + edu.getInstitution(),
                    new Font(Font.HELVETICA, 12, Font.BOLD)
            );

            doc.add(p);
        }

        doc.add(Chunk.NEWLINE);
    }

    private static void addSkills(Document doc, CvRequest cv) throws Exception {
        if (cv.getSkills() == null) return;

        addSectionTitle(doc, "Skills");

        Paragraph skills = new Paragraph(String.join(", ", cv.getSkills()));
        doc.add(skills);
    }

    private static void addSectionTitle(Document doc, String title) throws Exception {
        Font font = new Font(Font.HELVETICA, 14, Font.BOLD);

        Paragraph p = new Paragraph(title, font);
        p.setSpacingBefore(10);
        p.setSpacingAfter(5);

        doc.add(p);
    }
}
