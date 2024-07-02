package com.example.practicalwork.converters.fileconverters;

import com.example.practicalwork.configuration.Constants;
import com.example.practicalwork.models.DocField;
import com.example.practicalwork.models.Document;
import com.example.practicalwork.utils.field.DocFieldNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ToXlsxContract {

    private ToZipConverter toZipConverter;

    public void convert(Document doc) {

        try (Workbook workBook = new HSSFWorkbook()) {

            CellStyle style = workBook.createCellStyle();
            style.setVerticalAlignment(VerticalAlignment.TOP);

            Sheet sheet = workBook.createSheet(doc.getDocTitle().toString());

            // Create a title
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(style);
            titleCell.setCellValue(doc.getDocTitle().getLabel() + " № " + doc.getNumber());

            // Create top information
            Row topRow = sheet.createRow(1);
            Cell topCell11 = topRow.createCell(0);
            Cell topCell12 = topRow.createCell(1);
            topCell11.setCellStyle(style);
            topCell12.setCellStyle(style);

            topCell11.setCellValue(Constants.getCITY());
            topCell12.setCellValue(doc.getCreatedAt().toString().substring(0, 10));

            // Create header of document
            Row headerRow = sheet.createRow(2);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellStyle(style);
            headerCell.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_header")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue());

            // Create body
            int rowCount = 2;
            List<DocField> docFields = doc.getFields().stream()
                    .filter(el -> el.getName().contains("contract_div")).toList();

            for(DocField f : docFields) {
                    Row bodyRow = sheet.createRow(++rowCount);
                    Cell bodyCell = bodyRow.createCell(0);
                    bodyCell.setCellStyle(style);
                    bodyCell.setCellValue(f.getDefaultValue()
                            .replace("%n", ""));
            }

            // Create bottom information
            Row bottomRowDetails = sheet.createRow(++rowCount);
            Cell bottomRowDetailsCell11 = bottomRowDetails.createCell(0);
            Cell bottomRowDetailsCell12 = bottomRowDetails.createCell(1);
            bottomRowDetailsCell11.setCellStyle(style);
            bottomRowDetailsCell12.setCellStyle(style);
            bottomRowDetailsCell11.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_customer_details_col")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new)
                    .getDefaultValue().replace("%n", ""));
            bottomRowDetailsCell12.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_executor_details_col")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new)
                    .getDefaultValue().replace("%n", ""));

            Row bottomRowSign = sheet.createRow(++rowCount);
            Cell bottomRowSignCell11 = bottomRowSign.createCell(0);
            Cell bottomRowSignCell12 = bottomRowSign.createCell(1);
            bottomRowSignCell11.setCellStyle(style);
            bottomRowSignCell12.setCellStyle(style);


            bottomRowSignCell11.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_customer_sign")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new)
                    .getDefaultValue().replace("%n", ""));
            bottomRowSignCell12.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("contract_executor_sign")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new)
                    .getDefaultValue().replace("%n", ""));

            //Create file .xlsx
            try (FileOutputStream fos = new FileOutputStream(doc.getFile().getStore()
                    + doc.getFile().getName())) {

                workBook.write(fos);

            } catch(IOException e) {
                System.out.println("IOException : " + e);
            }

            //Compress file to .zip
            if (doc.getFile().isZip()) {

                toZipConverter.convert(doc.getFile());
            }

        } catch (IOException e) {

            System.out.println("IOException : " + e);
        }
    }
}
