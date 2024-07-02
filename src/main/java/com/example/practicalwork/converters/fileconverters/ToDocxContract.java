package com.example.practicalwork.converters.fileconverters;

import com.example.practicalwork.configuration.Constants;
import com.example.practicalwork.models.DocField;
import com.example.practicalwork.models.Document;
import com.example.practicalwork.utils.field.DocFieldNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ToDocxContract {

    private ToZipConverter toZipConverter;

    public void convert(Document doc) {

        try (XWPFDocument xwpfDocument = new XWPFDocument()) {

            // Create a title
            XWPFParagraph title = xwpfDocument.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);

            // Set title font
            XWPFRun titleRun = title.createRun();
            titleRun.setTextPosition(20);
            titleRun.setBold(true);
            titleRun.setFontSize(22);

            titleRun.setText(doc.getDocTitle().getLabel()
                    + " № " + doc.getNumber());

            // Create top table
            XWPFTable tableTop = xwpfDocument.createTable(1, 2);

            // Write to first row, first column
            XWPFParagraph p1 = tableTop.getRow(0).getCell(0).getParagraphs().get(0);
            p1.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun r1 = p1.createRun();
            r1.setTextPosition(20);
            r1.setText(Constants.getCITY());

            // Write to first row, second column
            XWPFParagraph p2 = tableTop.getRow(0).getCell(1).getParagraphs().get(0);
            p2.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun r2 = p2.createRun();
            r2.setTextPosition(20);
            r2.setText(doc.getCreatedAt().toString().substring(0, 10));


            // Create header
            XWPFParagraph header = xwpfDocument.createParagraph();
            header.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun headerRun = header.createRun();
            headerRun.setFontSize(12);
            headerRun.setText(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_header")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue());
            headerRun.addBreak();

            // Create body string
            StringBuilder data = new StringBuilder();
            List<DocField> docFields = doc.getFields().stream()
                    .filter(el -> el.getName().contains("contract_div")).toList();
            for (DocField f : docFields) {
                    data.append(f.getDefaultValue());
            }

            // Create body
            XWPFParagraph division1 = xwpfDocument.createParagraph();
            XWPFRun div1Run = division1.createRun();
            div1Run.setFontSize(12);

            if(data.toString().contains("%n")) {
                String[] lines = data.toString().split("%n");
                for(String str : lines) {
                    div1Run.setText(str);
                    div1Run.addBreak();
                }
            } else {
                div1Run.setText(data.toString(), 0);
            }
            div1Run.addBreak();

            // Create bottom table
            XWPFTable tableBottom = xwpfDocument.createTable(2, 2);

            // Write to first row, first column
            XWPFParagraph p11 = tableBottom.getRow(0).getCell(0).getParagraphs().get(0);
            XWPFRun p11Run = p11.createRun();
            p11Run.setFontSize(12);
            String p11Str = doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_customer_details_col")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue();

            String[] lines11 = p11Str.split("%n");
            for(String str : lines11) {
                p11Run.setText(str);
                p11Run.addBreak();
            }
            p11Run.addBreak();

            // Write to first row, second column
            XWPFParagraph p12 = tableBottom.getRow(0).getCell(1).getParagraphs().get(0);
            XWPFRun p12Run = p12.createRun();
            p12Run.setFontSize(12);
            String p12Str = doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_executor_details_col")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue();

            String[] lines12 = p12Str.split("%n");
            for(String str : lines12) {
                p12Run.setText(str);
                p12Run.addBreak();
            }
            p11Run.addBreak();

            // Write to second row, first column
            XWPFParagraph p21 = tableBottom.getRow(1).getCell(0).getParagraphs().get(0);
            XWPFRun p21Run = p21.createRun();
            p21Run.setFontSize(12);
            String p21Str = doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_customer_sign")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue();

            String[] lines21 = p21Str.split("%n");
            for(String str : lines21) {
                p21Run.setText(str);
                p21Run.addBreak();
            }

            // Write to second row, second column
            XWPFParagraph p22 = tableBottom.getRow(1).getCell(1).getParagraphs().get(0);
            XWPFRun p22Run = p22.createRun();
            p22Run.setFontSize(12);
            String p22Str = doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_executor_sign")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue();

            String[] lines22 = p22Str.split("%n");
            for(String str : lines22) {
                p22Run.setText(str);
                p22Run.addBreak();
            }

            //Create file
            try (FileOutputStream fos = new FileOutputStream(doc.getFile().getStore()
                    + doc.getFile().getName())) {

                xwpfDocument.write(fos);

            } catch(IOException e) {

                System.out.println("IOException : " + e);
            }

            //Compress file to .zip
            if (doc.getFile().isZip()) {

                toZipConverter.convert(doc.getFile());
            }

        } catch(IOException e) {

            System.out.println("IOException : " + e);
        }
    }
}