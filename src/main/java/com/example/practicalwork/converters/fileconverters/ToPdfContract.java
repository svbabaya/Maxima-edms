package com.example.practicalwork.converters.fileconverters;

import com.example.practicalwork.configuration.Constants;
import com.example.practicalwork.models.DocField;
import com.example.practicalwork.utils.field.DocFieldNotFoundException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class ToPdfContract {
    private ToZipConverter toZipConverter;
    public void convert(com.example.practicalwork.models.Document doc) {

        final String FONT = "fonts/HelveticaRegular.ttf";

        try {
            Document document = new Document(PageSize.A4);
            document.setMargins(72, 48, 48, 72);
            OutputStream os = new FileOutputStream(doc.getFile().getStore()
                    + doc.getFile().getName());

            PdfWriter.getInstance(document, os);
            document.open();

            // Create style
            Font fontTitle = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, true);
            fontTitle.setSize(18);
            Font fontBody = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, true);
            fontBody.setSize(11);

            // Create title
            String titleString = doc.getDocTitle().getLabel()
                    + " № " + doc.getNumber();

            Paragraph paragraphTitle = new Paragraph(titleString, fontTitle);
            paragraphTitle.setAlignment(Paragraph.ALIGN_CENTER);
            paragraphTitle.setSpacingAfter(20);
            document.add(paragraphTitle);

            // Create top table
            PdfPTable tableTop = new PdfPTable(2);
            tableTop.setWidthPercentage(100);
            tableTop.setSpacingAfter(20);

            PdfPCell cellT11 = new PdfPCell(new Phrase(Constants.getCITY(), fontBody));
            PdfPCell cellT12 = new PdfPCell(new Phrase(doc.getCreatedAt()
                    .toString().substring(0, 10), fontBody));
            cellT11.setBorder(Rectangle.NO_BORDER);
            cellT11.setVerticalAlignment(Element.ALIGN_TOP);
            cellT11.setHorizontalAlignment(Element.ALIGN_LEFT);

            cellT12.setBorder(Rectangle.NO_BORDER);
            cellT12.setVerticalAlignment(Element.ALIGN_TOP);
            cellT12.setHorizontalAlignment(Element.ALIGN_RIGHT);

            tableTop.addCell(cellT11);
            tableTop.addCell(cellT12);
            document.add(tableTop);

            // Create header of contract
            String headerString = doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_header")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue();

            Paragraph paragraphHeader = new Paragraph(headerString, fontBody);
            paragraphHeader.setAlignment(Paragraph.ALIGN_LEFT);
            paragraphHeader.setSpacingAfter(20);
            document.add(paragraphHeader);

            // Create body of contract
            StringBuilder body = new StringBuilder();
            List<DocField> docFields = doc.getFields().stream()
                    .filter(el -> el.getName().contains("contract_div")).toList();

            for(DocField f : docFields) {
                    body.append(f.getDefaultValue()
                            .replace("%n", "")).append("\n");
            }

            Paragraph paragraphBody = new Paragraph(body.toString(), fontBody);
            paragraphBody.setAlignment(Paragraph.ALIGN_LEFT);
            paragraphBody.setSpacingAfter(20);

            document.add(paragraphBody);

            // Create bottom table
            PdfPTable tableBottom = new PdfPTable(2);
            tableBottom.setWidthPercentage(100);
            tableBottom.setSpacingAfter(20);

            PdfPCell cellB11 = new PdfPCell(new Phrase(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_customer_details_col")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new)
                    .getDefaultValue().replace("%n", ""), fontBody));

            PdfPCell cellB12 = new PdfPCell(new Phrase(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_executor_details_col")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new)
                    .getDefaultValue().replace("%n", ""), fontBody));

            PdfPCell cellB21 = new PdfPCell(new Phrase(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_customer_sign")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new)
                    .getDefaultValue().replace("%n", ""), fontBody));

            PdfPCell cellB22 = new PdfPCell(new Phrase(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_executor_sign")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new)
                    .getDefaultValue().replace("%n", ""), fontBody));

            cellB11.setBorder(Rectangle.NO_BORDER);
            cellB11.setVerticalAlignment(Element.ALIGN_TOP);
            cellB11.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellB11.setPaddingBottom(20);

            cellB12.setBorder(Rectangle.NO_BORDER);
            cellB12.setVerticalAlignment(Element.ALIGN_TOP);
            cellB12.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellB12.setPaddingBottom(20);

            cellB21.setBorder(Rectangle.NO_BORDER);
            cellB21.setVerticalAlignment(Element.ALIGN_TOP);
            cellB21.setHorizontalAlignment(Element.ALIGN_LEFT);

            cellB22.setBorder(Rectangle.NO_BORDER);
            cellB22.setVerticalAlignment(Element.ALIGN_TOP);
            cellB22.setHorizontalAlignment(Element.ALIGN_LEFT);

            tableBottom.addCell(cellB11);
            tableBottom.addCell(cellB12);
            tableBottom.addCell(cellB21);
            tableBottom.addCell(cellB22);

            document.add(tableBottom);

            document.close();

        } catch (FileNotFoundException e) {

            System.out.println("FileNotFoundException : " + e);
        }

        //Compress file to .zip
        if (doc.getFile().isZip()) {

            toZipConverter.convert(doc.getFile());
        }

    }
}
