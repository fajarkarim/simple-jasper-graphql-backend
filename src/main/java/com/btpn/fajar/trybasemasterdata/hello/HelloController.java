package com.btpn.fajar.trybasemasterdata.hello;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@RestController
public class HelloController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping(path="/hello")
    public String hello () throws JRException, IOException {
        InputStream pdfStream = resourceLoader.getResource("classpath:jasper/hello.jrxml").getInputStream();
        JasperReport pdfReport = JasperCompileManager.compileReport(pdfStream);
        JRSaver.saveObject(pdfReport, "hello.jasper");
        JasperPrint pdfPrint = JasperFillManager.fillReport(pdfReport, null, new JREmptyDataSource());
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(pdfPrint);
        String blob = Base64.getEncoder().encodeToString(pdfBytes);
        return blob;
    }
}
