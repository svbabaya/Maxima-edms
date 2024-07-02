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
public class ToXlsxAct {

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

            // TODO Create information about contract
//            Row infoRow = sheet.createRow(1);
//            Cell infoCell = infoRow.createCell(0);
//            infoCell.setCellStyle(style);
//
            // TODO get information via related
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
                    .filter(el -> el.getName().equals("act_customer_details_row")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue());

            // Create executor details
            Row executorRow = sheet.createRow(3);
            Cell executorCell = executorRow.createCell(0);
            executorCell.setCellStyle(style);
            executorCell.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("act_executor_details_row")).findFirst()
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
            bodyRow1Cell3.setCellValue("Кол-во");

            Cell bodyRow1Cell4 = bodyRow1.createCell(3);
            bodyRow1Cell4.setCellStyle(style);
            bodyRow1Cell4.setCellValue("Цена");

            Cell bodyRow1Cell5 = bodyRow1.createCell(4);
            bodyRow1Cell5.setCellStyle(style);
            bodyRow1Cell5.setCellValue("НДС");

            Cell bodyRow1Cell6 = bodyRow1.createCell(5);
            bodyRow1Cell6.setCellStyle(style);
            bodyRow1Cell6.setCellValue("Сумма");

            Row bodyRow2 = sheet.createRow(5);

            Cell bodyRow2Cell1 = bodyRow2.createCell(0);
            bodyRow2Cell1.setCellStyle(style);
            bodyRow2Cell1.setCellValue("1");

            Cell bodyRow2Cell2 = bodyRow2.createCell(1);
            bodyRow2Cell2.setCellStyle(style);

            // TODO get information via related
            bodyRow2Cell2.setCellValue("%NameOfService%, Приложение 00001 от 22.02.2022, Договор 00001 от 22.02.2022");

            Cell bodyRow2Cell3 = bodyRow2.createCell(2);
            bodyRow2Cell3.setCellStyle(style);
            bodyRow2Cell3.setCellValue("1");

            Cell bodyRow2Cell4 = bodyRow2.createCell(3);
            bodyRow2Cell4.setCellStyle(style);
            bodyRow2Cell4.setCellValue("%CostOfServices%");

            Cell bodyRow2Cell5 = bodyRow2.createCell(4);
            bodyRow2Cell5.setCellStyle(style);
            bodyRow2Cell5.setCellValue("Без НДС");

            Cell bodyRow2Cell6 = bodyRow2.createCell(5);
            bodyRow2Cell6.setCellStyle(style);
            bodyRow2Cell6.setCellValue("%CostOfServices%");

            // Create information about cost
            Row costRow = sheet.createRow(6);
            Cell costRowCell = costRow.createCell(0);
            costRowCell.setCellStyle(style);
            costRowCell.setCellValue("Всего одно наименование на сумму %CostOfServices%");

            // Create final text
            Row finalRow = sheet.createRow(7);
            Cell finalRowCell = finalRow.createCell(0);
            finalRowCell.setCellStyle(style);
            finalRowCell.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("act_div_final")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new).getDefaultValue());

            // Create bottom information
            Row bottomRowSign = sheet.createRow(8);

            Cell bottomRowSignCell11 = bottomRowSign.createCell(0);
            Cell bottomRowSignCell12 = bottomRowSign.createCell(1);
            bottomRowSignCell11.setCellStyle(style);
            bottomRowSignCell12.setCellStyle(style);
            bottomRowSignCell11.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("act_customer_sign")).findFirst()
                    .orElseThrow(DocFieldNotFoundException::new)
                    .getDefaultValue().replace("%n", ""));

            bottomRowSignCell12.setCellValue(doc.getFields().stream()
                    .filter(el -> el.getName().equals("act_executor_sign")).findFirst()
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
