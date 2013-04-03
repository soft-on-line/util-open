package org.open.util;

import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

public class PdfUtil {

    private static final Log log = LogFactory.getLog(PdfUtil.class);

    public static void main(String[] args) {
        String[] files = { "C:\\a.pdf", "C:\\b.pdf" };
        String savepath = "C:\\temp.pdf";
        mergePdf(files, savepath);

        splitPdf("C:\\temp.pdf", 1);
    }

    public static void mergePdf(String[] pdf, String savePdf) {
        if (null == pdf || pdf.length < 1 || null == savePdf) {
            return;
        }

        Document document = null;
        try {
            document = new Document(new PdfReader(pdf[0]).getPageSize(1));

            PdfCopy copy = new PdfCopy(document, new FileOutputStream(savePdf));

            document.open();

            for (int i = 0; i < pdf.length; i++) {
                PdfReader reader = new PdfReader(pdf[i]);

                int n = reader.getNumberOfPages();

                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }

            document.close();

        } catch (Exception e) {

            StringBuffer buf = new StringBuffer("PDF:");
            for (String file : pdf) {
                buf.append(file).append(",");
            }

            log.error("mergePdf(" + buf.toString() + "savePdf:" + savePdf + ") error!", e);
        } finally {
            if (null != document) {
                document.close();
            }
        }
    }

    public static void splitPdf(String pdf, int minPageSize) {
        if (minPageSize < 1) {
            return;
        }
        Document document = null;
        PdfCopy copy = null;

        try {
            PdfReader reader = new PdfReader(pdf);

            int n = reader.getNumberOfPages();

            if (n < minPageSize) {
                log.warn("The document does not have " + minPageSize + " pages to partition !");

                return;
            }

            int size = n / minPageSize;
            String staticpath = pdf.substring(0, pdf.lastIndexOf("\\") + 1);
            String savepath = null;
            ArrayList<String> savepaths = new ArrayList<String>();
            for (int i = 1; i <= minPageSize; i++) {
                if (i < 10) {
                    savepath = pdf.substring(pdf.lastIndexOf("\\") + 1, pdf.length() - 4);
                    savepath = staticpath + savepath + "0" + i + ".pdf";
                    savepaths.add(savepath);
                } else {
                    savepath = pdf.substring(pdf.lastIndexOf("\\") + 1, pdf.length() - 4);
                    savepath = staticpath + savepath + i + ".pdf";
                    savepaths.add(savepath);
                }
            }

            for (int i = 0; i < minPageSize - 1; i++) {
                document = new Document(reader.getPageSize(1));
                copy = new PdfCopy(document, new FileOutputStream(savepaths.get(i)));
                document.open();
                for (int j = size * i + 1; j <= size * (i + 1); j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
                document.close();
            }

            document = new Document(reader.getPageSize(1));
            copy = new PdfCopy(document, new FileOutputStream(savepaths.get(minPageSize - 1)));
            document.open();
            for (int j = size * (minPageSize - 1) + 1; j <= n; j++) {
                document.newPage();
                PdfImportedPage page = copy.getImportedPage(reader, j);
                copy.addPage(page);
            }
            document.close();

        } catch (Exception e) {
            log.error("splitPdf(PDF:" + pdf + ",minPageSize:" + minPageSize + ") error!", e);
        } finally {
            if (null != document) {
                document.close();
            }
        }
    }

}
