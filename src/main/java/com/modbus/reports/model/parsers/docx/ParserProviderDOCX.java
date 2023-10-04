package com.modbus.reports.model.parsers.docx;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.Desktop;
import java.util.concurrent.TimeUnit;

public class ParserProviderDOCX implements ParserProvider {
    private Path path;
    String fileWithoutExtension;

    @Override
    public void readDocument(Path path, TableView<Variables> tableView) throws IOException {
        this.path = path;
        XWPFDocument doc;
        try {
            doc = new XWPFDocument(OPCPackage.open(path.toString()));
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        if (!doc.getParagraphs().isEmpty()) {
            for (XWPFParagraph p : doc.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0).trim();
                        for (int i = 0; i < tableView.getItems().size(); i++) {
                            text = text.replace("{" + tableView.getItems().get(i).getAlias() + "}", tableView.getItems().get(i).getValue());
                            r.setText(text, 0);
                        }
                        text = text.replace("{time}", formatter.format(now));
                        r.setText(text,0);
                    }
                }
            }
        }
        //if(!doc.getTables().isEmpty()) {
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            for (int i = 0; i < tableView.getItems().size(); i++) {
                                if (text != null && text.contains("{" + tableView.getItems().get(i).getAlias() + "}")) {
                                    text = text.replace("{" + tableView.getItems().get(i).getAlias() + "}", tableView.getItems().get(i).getValue());
                                    r.setText(text, 0);
                                }
                            }
                            if (text != null && text.contains("{time}")) {
                                text = text.replace("{time}", formatter.format(now));
                            }
                            r.setText(text,0);
                        }
                    }
                }
            }
        }
        //}
        int dotIndex = path.getFileName().toString().lastIndexOf('.');
        fileWithoutExtension = path.getFileName().toString().substring(0, dotIndex);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("src/main/resources/outParsing/" + fileWithoutExtension + "_out.docx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            doc.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fos.close();
            TimeUnit.MILLISECONDS.sleep(300);
            open();
        } catch (FileNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void open() throws IOException {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        assert desktop != null;
        if (System.getProperty("os.name").equals("Linux")
                && System.getProperty("java.vendor").startsWith("Read Hat")) {
            new ProcessBuilder("xdg-open", "src/main/resources/outParsing/" + fileWithoutExtension + "_out.docx");
        } else {
            Desktop.getDesktop().open(new File("src/main/resources/outParsing/" + fileWithoutExtension + "_out.docx"));
        }
    }

}