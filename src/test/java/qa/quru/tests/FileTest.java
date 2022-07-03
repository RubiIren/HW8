package qa.quru.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.pdftest.matchers.ContainsExactText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileTest {

    ClassLoader cl = getClass().getClassLoader();

    @Test
    void parseZipTest() throws Exception {
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("zip/HW8.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().equals("HW8/FileXLSX.xlsx")) {
                XLS xls = new XLS(is);
                String value = xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
                assertThat(value).contains("Базовые области");
            } else if (entry.getName().equals("HW8/FilePDF.pdf")) {
                PDF pdf = new PDF(is);
                Assertions.assertEquals(1, pdf.numberOfPages);
                assertThat(pdf, new ContainsExactText("Региональный оператор по обращению с отходами в Ленинградской области"));
            } else if (entry.getName().equals("HW8/FileCSV.csv")) {
                CSVReader reader = new CSVReader(new InputStreamReader(is));
                List<String[]> content = reader.readAll();
                org.assertj.core.api.Assertions.assertThat(content).contains(
                        new String[]{"What", "Whom"},
                        new String[]{"Kot", "Babushkin"},
                        new String[]{"Lego", "Kota"});
            }
        }
    }


    @Test
    void parseXLSTest() throws Exception {
        InputStream stream = cl.getResourceAsStream("xls/FileXLSX.xlsx");
        XLS xls = new XLS(stream);
        String value = xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
        assertThat(value).contains("Базовые области");
    }
}

