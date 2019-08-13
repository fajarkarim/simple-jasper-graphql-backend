package com.btpn.fajar.trybasemasterdata.hello;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class HelloController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping(path="/hello")
    public HelloDTO hello () throws JRException, IOException {
        // Read jrxml file
        InputStream pdfStream = resourceLoader.getResource("classpath:jasper/hello.jrxml").getInputStream();

        // create jasper report
        JasperReport pdfReport = JasperCompileManager.compileReport(pdfStream);

        // create parameters for jasper file, for example key parameter FAJAR_TITLE with "junkies" as value
        HashMap<String, Object> mainParameters = new HashMap<>();
        mainParameters.put("FAJAR_TITLE", "junkies");
        JasperPrint pdfPrint = JasperFillManager.fillReport(pdfReport, mainParameters, new JREmptyDataSource());

        // convert jasper file to pdf
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(pdfPrint);

        // convert pdf to base64
        String blob = Base64.getEncoder().encodeToString(pdfBytes);
        return new HelloDTO(blob);
    }
}
