package com.example.practicalwork.converters.fileconverters;

import com.example.practicalwork.configuration.Constants;
import com.example.practicalwork.models.Document;
import com.example.practicalwork.utils.field.DocFieldNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@AllArgsConstructor
public class ToXlsxAccount {

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

//            // Create information about contract
//            Row infoRow = sheet.createRow(1);
//            Cell infoCell = infoRow.createCell(0);
//            infoCell.setCellStyle(style);
//
//            // TODO get information via related
//            infoCell.setCellValue("к Договору № 0001 от 22.02.2022");

            // Create top information
            Row topRow = sheet.createRow(1);
            Cell topCell11 = topRow.createCell(0);
            Cell topCell12 = topRow.createCell(1);
            topCell11.setCellStyle(style);
            topCell12.setCellStyle(style);

            topCell11.setCellValue(Constants.getCITY());
            topCell12.setCellValue(doc.getCreatedAt().toString().substring(0, 10));

            // Create customer details
            Row customerRow = sheet.createRow(2);
            Cell customerCell = customerRow.createCell(0);
            customerCell.setCellStyle(style);
            customerCell.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("account_customer_details_row")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue());

            // Create executor details
            Row executorRow = sheet.createRow(3);
            Cell executorCell = executorRow.createCell(0);
            executorCell.setCellStyle(style);
            executorCell.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("account_executor_details_row")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue());

            // Create body
            Row bodyRow1 = sheet.createRow(4);

            Cell bodyRow1Cell1 = bodyRow1.createCell(0);
            bodyRow1Cell1.setCellStyle(style);
            bodyRow1Cell1.setCellValue("№");

            Cell bodyRow1Cell2 = bodyRow1.createCell(1);
            bodyRow1Cell2.setCellStyle(style);
            bodyRow1Cell2.setCellValue("Наименование услуги (работы)");

            Cell bodyRow1Cell3 = bodyRow1.createCell(2);
            bodyRow1Cell3.setCellStyle(style);
            bodyRow1Cell3.setCellValue("Стоимость услуг, руб., без НДС");

            Row bodyRow2 = sheet.createRow(5);

            Cell bodyRow2Cell1 = bodyRow2.createCell(0);
            bodyRow2Cell1.setCellStyle(style);
            bodyRow2Cell1.setCellValue("1");

            Cell bodyRow2Cell2 = bodyRow2.createCell(1);
            bodyRow2Cell2.setCellStyle(style);

            // TODO get information via related
            bodyRow2Cell2.setCellValue("%NameOfService%, Приложение 00001 от 22.02.2022, Договор 00001 от 22.02.2022");

            Cell bodyRow2Cell6 = bodyRow2.createCell(2);
            bodyRow2Cell6.setCellStyle(style);
            bodyRow2Cell6.setCellValue("%CostOfServices%");

            // Create information about cost
            Row costRow = sheet.createRow(6);
            Cell costRowCell = costRow.createCell(0);
            costRowCell.setCellStyle(style);
            costRowCell.setCellValue("Сумма прописью: %CostOfServicesByWords%, без налога (НДС)");

            // Create bottom information
            Row signRow = sheet.createRow(7);
            Cell signRowCell = signRow.createCell(0);
            signRowCell.setCellStyle(style);
            signRowCell.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("account_executor_sign")).findFirst()
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
