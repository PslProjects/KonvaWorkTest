package com.scada.controller;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.scada.entities.ResultRecord;
import com.scada.service.ResultService;
import com.scada.sess.AdminSessionTracker;

import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/result")
@CrossOrigin(origins = {"https://scada.pratikshat.com", "http://198.7.114.147:9999", "http://localhost:9999", "http://localhost:4200"})
public class ResultController {

    @Autowired
    private ResultService service;

    @PostMapping("/save")
    public ResultRecord saveResult(@RequestBody ResultRecord record) {

        String admin = AdminSessionTracker.getCurrentAdmin();
        record.setAdminName(admin != null ? admin : "NO_ADMIN");

        long now = System.currentTimeMillis();

        if (record.getStartTime() == null)
            record.setStartTime(now);

        if (record.getEndTime() == null)
            record.setEndTime(now);

        if (record.getTimeTakenMs() == null)
            record.setTimeTakenMs(record.getEndTime() - record.getStartTime());

        return service.save(record);
    }


    @GetMapping("/user/{username}")
    public ResponseEntity<Page<ResultRecord>> getByUser(
            @PathVariable("username") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(service.findByUser(username, page, size));
    }


    @GetMapping("/{id}")
    public ResultRecord getById(@PathVariable("id") Long id) {
        ResultRecord record = service.findById(id).orElse(null);
        if (record == null) return null;
        return record;
    }

//    @GetMapping("/all")
//    public List<ResultRecord> getAll() {
//
//        return service.findAll();
//    }
//    public ResponseEntity<Page<ResultRecord>> getAll(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size) {
//
//        return ResponseEntity.ok(service.findAll(page, size));
//    }

    //
//    @GetMapping("/all")
//    public Page<ResultRecord> getAll(
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam(required = false) Long startDate,
//            @RequestParam(required = false) Long endDate) {
//
//        return service.findAllWithDateFilter(page, size, startDate, endDate);
//    }
    @GetMapping("/all")
    public Page<ResultRecord> getAll(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {

        return service.findAllWithDateFilter(page, size, startDate, endDate);
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats(
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {

        return service.getStats(startDate, endDate);
    }



    @GetMapping("/session/{sessionId}")
    public ResultRecord getResultBySession(@PathVariable String sessionId) {
        ResultRecord record = service.findBySessionId(sessionId);
        return record;
    }



    @GetMapping("/download-pdf")
    public void downloadPdf(
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate,
            HttpServletResponse response) throws Exception {

        List<ResultRecord> results =
                service.findAllForPdf(startDate, endDate);

        response.setContentType("application/pdf");
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy hh:mm a"
                );

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=scada-report.pdf"
        );

        Document document =
                new Document(
                        PageSize.A4.rotate(),
                        20,
                        20,
                        20,
                        20
                );

        PdfWriter.getInstance(
                document,
                response.getOutputStream()
        );

        document.open();

        // ⭐ Fonts
        Font titleFont =
                new Font(Font.HELVETICA, 22, Font.BOLD);

        Font subFont =
                new Font(Font.HELVETICA, 11, Font.NORMAL);

        Font headerFont =
                new Font(Font.HELVETICA, 11, Font.BOLD);

        Font cellFont =
                new Font(Font.HELVETICA, 10, Font.NORMAL);

        Font passFont =
                new Font(Font.HELVETICA, 10, Font.BOLD);

        Font failFont =
                new Font(Font.HELVETICA, 10, Font.BOLD);

        passFont.setColor(Color.GREEN);

        failFont.setColor(Color.RED);

        // ⭐ Title
        Paragraph title =
                new Paragraph(
                        "SCADA Results Report",
                        titleFont
                );

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        // ⭐ Subtitle
        Paragraph sub =
                new Paragraph(
                        "Generated on: " + new Date(),
                        subFont
                );

        sub.setAlignment(Element.ALIGN_CENTER);

        document.add(sub);

        document.add(new Paragraph(" "));

        // ⭐ Table
        PdfPTable table =
                new PdfPTable(9);

        table.setWidthPercentage(100);

        table.setSpacingBefore(10);

        table.setWidths(
                new float[]{
                        1.2f,
                        1.5f,
                        2f,
                        1.5f,
//                        2.5f,
                        1.3f,
                        1.5f,
                        1f,
                        1.5f,
                        2f
                }
        );

        // ⭐ Header color
        Color headerBg =
                new Color(30, 41, 59);

        // ⭐ Helper for header cells
        String[] headers = {
                "Status",
                "User",
                "Board",
                "Line Id",
//                "Fault Area",
                "Success",
                "Time",
                "FCB Count",
                "Admin",
                "Date"
        };

        for (String h : headers) {

            PdfPCell cell =
                    new PdfPCell(
                            new Phrase(h, headerFont)
                    );

            cell.setBackgroundColor(headerBg);

            cell.setHorizontalAlignment(
                    Element.ALIGN_CENTER
            );

            cell.setPadding(8);

            cell.setBorderWidth(1);

            cell.getPhrase().getFont()
                    .setColor(Color.WHITE);

            table.addCell(cell);
        }

        // ⭐ Rows
        for (ResultRecord r : results) {

            String branchBoard = "-";

            try {

                if (r.getUserSequence() != null) {

                    String seq =
                            r.getUserSequence().trim();

                    if (seq.startsWith("[")) {

                        int firstQuote =
                                seq.indexOf("\"");

                        int secondQuote =
                                firstQuote >= 0
                                        ? seq.indexOf("\"", firstQuote + 1)
                                        : -1;

                        if (firstQuote >= 0 && secondQuote > firstQuote) {

                            seq =
                                    seq.substring(firstQuote + 1, secondQuote);
                        }
                    }

                    int idx =
                            seq.indexOf("[");

                    if (idx > 0) {

                        branchBoard =
                                seq.substring(0, idx)
                                        .trim();
                    }
                }

            } catch (Exception e) {

                branchBoard = "-";
            }

            String mainBoard = "-";
//            String faultArea = "-";

            if (r.getFaultLine() != null
                    && !r.getFaultLine().trim().isEmpty()) {

                String boardArea =
                        r.getFaultLine().trim();

                int splitIndex =
                        boardArea.indexOf(" ");

                if (splitIndex > 0) {

                    mainBoard =
                            boardArea.substring(0, splitIndex)
                                    .trim();

//                    faultArea =
//                            boardArea.substring(splitIndex + 1)
//                                    .trim();

                } else {

                    mainBoard = boardArea;
                }
            }

            // ⭐ Status Color
            Font statusFont =
                    "PASS".equalsIgnoreCase(
                            r.getStatus()
                    )
                            ? passFont
                            : failFont;

            PdfPCell statusCell =
                    new PdfPCell(
                            new Phrase(
                                    String.valueOf(
                                            r.getStatus()
                                    ),
                                    statusFont
                            )
                    );

            statusCell.setHorizontalAlignment(
                    Element.ALIGN_CENTER
            );

            statusCell.setPadding(6);

            table.addCell(statusCell);

            // ⭐ User
            table.addCell(
                    new Phrase(
                            String.valueOf(
                                    r.getUserName()
                            ),
                            cellFont
                    )
            );

            // ⭐ Branch Board
            table.addCell(
                    new Phrase(
                            branchBoard,
                            cellFont
                    )
            );

            // ⭐ Main Board
            table.addCell(
                    new Phrase(
                            mainBoard,
                            cellFont
                    )
            );

            // ⭐ Fault Area
//            table.addCell(
//                    new Phrase(
//                            faultArea,
//                            cellFont
//                    )
//            );

            // ⭐ Success %
            table.addCell(
                    new Phrase(
                            r.getResultPercentage()
                                    + "%",
                            cellFont
                    )
            );

            // ⭐ Time Taken
            String time =
                    (r.getTimeTakenMs() / 1000.0)
                            + " sec";

            table.addCell(
                    new Phrase(
                            time,
                            cellFont
                    )
            );

            // ⭐ FCB Count
            table.addCell(
                    new Phrase(
                            String.valueOf(
                                    r.getFcbFlipCount()
                            ),
                            cellFont
                    )
            );

            // ⭐ Admin
            table.addCell(
                    new Phrase(
                            String.valueOf(
                                    r.getAdminName()
                            ),
                            cellFont
                    )
            );

            // ⭐ Date
            // ⭐ Date Formatting

            String formattedDate = "-";

            try {

                if (r.getCreatedAt() != null) {

                    formattedDate =
                            r.getCreatedAt()
                                    .atZone(
                                            ZoneId.systemDefault()
                                    )
                                    .format(formatter);
                }

            } catch (Exception e) {

                formattedDate = "-";
            }

            table.addCell(
                    new Phrase(
                            formattedDate,
                            cellFont
                    )
            );
        }

        document.add(table);

        // ⭐ Footer
        document.add(new Paragraph(" "));

        Paragraph footer =
                new Paragraph(
                        "SCADA Monitoring System Report",
                        subFont
                );

        footer.setAlignment(
                Element.ALIGN_RIGHT
        );

        document.add(footer);

        document.close();
    }

}
