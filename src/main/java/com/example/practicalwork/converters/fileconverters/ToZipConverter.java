package com.example.practicalwork.converters.fileconverters;

import com.example.practicalwork.models.DocFile;
import com.example.practicalwork.services.DocFileService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class ToZipConverter {

    private final DocFileService docFileService;
    public void convert(DocFile docFile) {

        try {
            String zipFileName = StringUtils.substringBefore(docFile.getName(), ".")
                    + ".zip";
            createZip(docFile.getStore() + docFile.getName(), zipFileName);
            docFile.setName(zipFileName);
            docFileService.write(docFile);
        } catch(IOException e) {
            System.out.println("IOException : " + e);
        }
    }

    public void createZip(String source, String target) throws IOException {

        FileOutputStream fout = new FileOutputStream(target);
        ZipOutputStream zout = new ZipOutputStream(fout);

        FileInputStream fin = new FileInputStream(source);
        ZipEntry zipEntry = new ZipEntry(source);
        zout.putNextEntry(zipEntry);
        int length;
        byte[] buffer = new byte[1024];
        while((length = fin.read(buffer)) > 0) {
            zout.write(buffer, 0, length);
        }

        zout.closeEntry();
        zout.finish();
        fin.close();
        zout.close();

    }

}
