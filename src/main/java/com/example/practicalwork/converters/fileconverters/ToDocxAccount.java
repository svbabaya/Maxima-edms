package com.example.practicalwork.converters.fileconverters;

import com.example.practicalwork.configuration.Constants;
import com.example.practicalwork.models.Document;
import com.example.practicalwork.utils.field.DocFieldNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@AllArgsConstructor
public class ToDocxAccount {
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

            titleRun.setText(doc.getDocTitle().getLabel() + " № " + doc.getNumber());

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

            // Create customer details
            XWPFParagraph customerDetails = xwpfDocument.createParagraph();
            customerDetails.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun customerRun = customerDetails.createRun();
            customerRun.setFontSize(12);
            customerRun.setText(doc.getFields().stream()
                    .filter(el -> el.getName().equals("account_customer_details_row")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue());
            customerRun.addBreak();

            // Create executor details
            XWPFParagraph executorDetails = xwpfDocument.createParagraph();
            executorDetails.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun executorRun = executorDetails.createRun();
            executorRun.setFontSize(12);
            executorRun.setText(doc.getFields().stream()
                    .filter(el -> el.getName().equals("account_executor_details_row")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue());
            executorRun.addBreak();
            executorDetails.setBorderBottom(Borders.BASIC_BLACK_DASHES);

            // Create body table
            XWPFTable tableBody = xwpfDocument.createTable(2, 3);

            // Write to first row
            XWPFParagraph top11 = tableBody.getRow(0).getCell(0).getParagraphs().get(0);
            XWPFRun topRun11 = top11.createRun();
            topRun11.setFontSize(12);
            topRun11.setText("№");

            XWPFParagraph top12 = tableBody.getRow(0).getCell(1).getParagraphs().get(0);
            XWPFRun topRun12 = top12.createRun();
            topRun12.setFontSize(12);
            topRun12.setText("Наименование услуги (работы)");

            XWPFParagraph top13 = tableBody.getRow(0).getCell(2).getParagraphs().get(0);
            XWPFRun topRun13 = top13.createRun();
            topRun13.setFontSize(12);
            topRun13.setText("Стоимость услуг, руб., без НДС");

            tableBody.getRow(1).getCell(0).setText("1");

            // TODO get information via related
            tableBody.getRow(1).getCell(1).setText("%NameOfService%, Приложение 00001 от 22.02.2022, Договор 00001 от 22.02.2022");
            tableBody.getRow(1).getCell(2).setText("%CostOfServices%");

            // Create information about cost
            XWPFParagraph cost = xwpfDocument.createParagraph();
            XWPFRun costRun = cost.createRun();
            costRun.setFontSize(12);
            costRun.addBreak();
            costRun.setText("Сумма прописью: %CostOfServicesByWords%, без налога (НДС)");
            cost.setBorderTop(Borders.BASIC_BLACK_DASHES);
            costRun.addBreak();

            // Create sign place
            XWPFParagraph sign = xwpfDocument.createParagraph();
            XWPFRun signRun = sign.createRun();
            signRun.setFontSize(12);
            String signStr = doc.getFields().stream()
                    .filter(el -> el.getName().equals("account_executor_sign")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue();

            String[] lines = signStr.split("%n");
            for(String str : lines) {
                signRun.setText(str);
                signRun.addBreak();
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