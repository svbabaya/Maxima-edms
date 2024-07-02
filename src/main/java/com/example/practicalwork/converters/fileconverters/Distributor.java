package com.example.practicalwork.converters.fileconverters;

import com.example.practicalwork.configuration.Constants;
import com.example.practicalwork.models.DocFile;
import com.example.practicalwork.models.Document;
import com.example.practicalwork.services.DocFileService;
import com.example.practicalwork.services.DocumentService;
import com.example.practicalwork.utils.FileNameCreator;
import com.example.practicalwork.utils.FileSizeCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Distributor {

    private final ToDocxContract toDocxContract;
    private final ToDocxApplication toDocxApplication;
    private final ToDocxAct toDocxAct;
    private final ToDocxAccount toDocxAccount;
    private final ToXlsxContract toXlsxContract;
    private final ToXlsxApplication toXlsxApplication;
    private final ToXlsxAct toXlsxAct;
    private final ToXlsxAccount toXlsxAccount;
    private final ToPdfContract toPdfContract;
    private final FileNameCreator fileNameCreator;
    private final FileSizeCreator fileSizeCreator;
    private final DocFileService docFileService;
    private final DocumentService docService;

    public void createFileOnDisk(Document doc, DocFile docFile) {

        doc.setFile(docFile);
        docService.write(doc);

        docFile.setName(fileNameCreator.create(doc));
        docFile.setStore(Constants.getSTORE());

        // Create files .docx/.xlsx/.pdf/.zip and update repository
        String str = doc.getDocTitle().toString() + "_" + docFile.getExtension().toString();
        switch (str) {
            case "CONTRACT_DOCX" -> toDocxContract.convert(doc);
            case "CONTRACT_XLSX" -> toXlsxContract.convert(doc);
            case "APPLICATION_DOCX" -> toDocxApplication.convert(doc);
            case "APPLICATION_XLSX" -> toXlsxApplication.convert(doc);
            case "ACT_DOCX" -> toDocxAct.convert(doc);
            case "ACT_XLSX" -> toXlsxAct.convert(doc);
            case "ACCOUNT_DOCX" -> toDocxAccount.convert(doc);
            case "ACCOUNT_XLSX" -> toXlsxAccount.convert(doc);
            case "CONTRACT_PDF" -> toPdfContract.convert(doc);

            // TODO converters to PDF for application, act, account
//            case "APPLICATION_PDF" -> toPdfContract.convert(doc);
//            case "ACT_PDF" -> toPdfContract.convert(doc);
//            case "ACCOUNT_PDF" -> toPdfContract.convert(doc);

        }
        docFile.setSize(fileSizeCreator.create(docFile));
        docFileService.write(docFile);
    }
}
