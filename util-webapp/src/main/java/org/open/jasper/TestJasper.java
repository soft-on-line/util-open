package org.open.jasper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JasperDesignFactory;

public class TestJasper {

    private static class MapDataSource implements JRDataSource {

        private List<Map<String, Object>> listMap;
        private int                       index = -1;

        public MapDataSource(List<Map<String, Object>> listMap) {
            this.listMap = listMap;
        }

        public Object getFieldValue(JRField jrField) throws JRException {
            Map<String, Object> map = listMap.get(index);
            return (null == map) ? null : map.get(jrField.getName());
        }

        public boolean next() throws JRException {
            if (null == listMap) {
                return false;
            }
            return ++index < listMap.size();
        }
    }

    public static void test(String template) throws ClassNotFoundException {
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        try {
            long st = System.currentTimeMillis();

            JasperDesign jasperDesign = JRXmlLoader.load(template);

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("d://report.jasper"));
            oos.writeObject(jasperDesign);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("d://report.jasper"));
            jasperDesign = (JasperDesign) ois.readObject();
            ois.close();

            // jasperReport = JasperCompileManager.compileReport(template);
            // jasperReport = JasperCompileManager.compileReport(ClassLoader.getSystemResourceAsStream("report.jrxml"));
            jasperReport = JasperCompileManager.compileReport(jasperDesign);

            System.out.println("compileReport UsedTime:" + (System.currentTimeMillis() - st));

            // jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), new JREmptyDataSource());
            // JasperExportManager.exportReportToPdfFile(jasperPrint, "d:/1.pdf");

            List<Map<String, Object>> listRecords = new ArrayList<Map<String, Object>>();
            int count = 5;
            for (int i = 0; i < count; i++) {
                Map<String, Object> listFields = new HashMap<String, Object>();

                for (int j = 1; j <= 18; j++) {
                    listFields.put("Column" + j, "Column" + i + "-" + j);
                }

                listRecords.add(listFields);
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("TableDataSource", new MapDataSource(listRecords));

            // params.put("TableDataSource", new JREmptyDataSource(10));
            // JasperFillManager.fillReportToFile("d:/TableReport.jasper", params);

            jasperPrint = JasperFillManager.fillReport(jasperReport, params);

            JasperExportManager.exportReportToHtmlFile(jasperPrint, "d:/1.html");

            JRAbstractExporter exporter = new JExcelApiExporter();
            FileOutputStream output = null;

            output = new FileOutputStream("d:/excel.xls");

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);

            exporter.exportReport();

            output.close();

        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException {
        // String template = "d:/jasperreports_demo.jrxml";
        // String template = "d:/TableReport.jrxml";
        // String template = "d:/TabularReport.jrxml";
        // String template = "d:/jxforestry.jrxml";
        String template = "d:/report.jrxml";
        // String template = "D:/workspace/eclipse/Hundsun/jx-forestry/jxforestry/web/src/main/resources/report.jrxml";

        test(template);
    }
}
