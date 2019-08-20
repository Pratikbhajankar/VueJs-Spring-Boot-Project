package com.inventory.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.awt.print.PrinterJob;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Stream;

@Service @Slf4j public class PrinterUtility {

  private static boolean jobRunning = true;

  public static void generatePdf(String printerName)
      throws IOException, DocumentException, URISyntaxException, PrintException,
      InterruptedException {
    Document document = new Document();
    PdfWriter.getInstance(document,
        new FileOutputStream(new ClassPathResource("GenerateBill.pdf").getFile()));
    document.open();
    Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
    Chunk chunk = new Chunk("Hello World", font);
    document.add(chunk);
    document.close();
    final InputStream inputStream = new ClassPathResource("GenerateBill.pdf").getInputStream();
    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

    final PrintService[] printServicesList = PrinterJob.lookupPrintServices();
    final Optional<PrintService> printServiceOptional = Stream.of(printServicesList)
        .filter(ps -> ps.getName().contains(printerName)).findFirst();
    final PrintService printService = printServiceOptional
        .orElseThrow(() -> new RuntimeException("No filter found with give name"));
    final DocPrintJob printJob = printService.createPrintJob();

    printJob.addPrintJobListener(new JobCompleteMonitor());
    Doc doc = new SimpleDoc(inputStream, flavor, null);
    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
    aset.add(OrientationRequested.PORTRAIT);
    aset.add(new JobName("Ammmron - Document", null));

    printJob.print(doc, aset);
    while (jobRunning) {
      Thread.sleep(1000);
    }
    System.out.println("Exiting app");
    inputStream.close();

  }

  private static class JobCompleteMonitor extends PrintJobAdapter {
    @Override public void printJobCompleted(PrintJobEvent jobEvent) {
      System.out.println("Job completed");
      jobRunning = false;
    }
  }
}
