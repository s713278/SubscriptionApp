package com.app.services;

import com.app.entites.Order;
import com.app.payloads.OrderDetailsDTO;
import com.app.repositories.RepositoryManager;
import com.app.services.impl.SkuService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderService {

  private final RepositoryManager repositoryManager;
  private final JavaMailSender mailSender;
  private final SkuService skuService;
  private final ServiceManager serviceManager;

  public List<OrderDetailsDTO> getUserOrderHistory(Long userId) {
    return get(repositoryManager.getOrderRepo().findAllBySubscriptionUserId(userId));
  }

  public List<OrderDetailsDTO> getOrderHistoryByDateRange(
      Long userId, LocalDate startDate, LocalDate endDate) {
    var orders =
        repositoryManager
            .getOrderRepo()
            .findBySubscriptionUserIdAndDateRange(userId, startDate, endDate);
    return get(orders);
  }

  public Resource exportOrderHistoryToCsv(Long userId, LocalDate startDate, LocalDate endDate) {
    List<OrderDetailsDTO> orderHistory = getOrderHistoryByDateRange(userId, startDate, endDate);
    try (StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter =
            new CSVPrinter(
                writer,
                CSVFormat.DEFAULT.withHeader(
                    "Id",
                    "Delivery Date",
                    "Vendor Name",
                    "Item Name",
                    "Size",
                    "Quantity",
                    "Amount",
                    "Discount",
                    "Total Amount",
                    "Total Discount"))) {
      for (OrderDetailsDTO order : orderHistory) {
        csvPrinter.printRecord(
            order.getOrderId(),
            order.getDeliveryDate(),
            order.getVendorName(),
            order.getItemName(),
            order.getSize(),
            order.getQuantity(),
            order.getAmount(),
            order.getDiscount(),
            order.getTotalAmount(),
            order.getTotalDiscount());
      }
      csvPrinter.flush();
      byte[] csvData = writer.toString().getBytes(StandardCharsets.UTF_8);
      return new ByteArrayResource(csvData);
    } catch (IOException e) {
      throw new RuntimeException("Failed to export order history to CSV", e);
    }
  }

  public void sendOrderHistoryByEmail(
      Long userId, LocalDate startDate, LocalDate endDate, String email) {
    Resource csvResource = exportOrderHistoryToCsv(userId, startDate, endDate);

    MimeMessage message = mailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(email);
      helper.setSubject("Order History Report");
      helper.setText("Please find attached the order history report.");

      InputStreamSource csvAttachment =
          new ByteArrayResource(csvResource.getInputStream().readAllBytes());
      helper.addAttachment("order-history.csv", csvAttachment);

      mailSender.send(message);

    } catch (MessagingException | IOException e) {
      throw new RuntimeException("Failed to send email with order history", e);
    }
  }

  public Resource exportOrderHistoryToPdf(Long userId, LocalDate startDate, LocalDate endDate) {
    List<OrderDetailsDTO> orderHistory = getOrderHistoryByDateRange(userId, startDate, endDate);

    Document document = new Document();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      PdfWriter.getInstance(document, out);
      document.open();

      // Add Title
      Font fontTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
      Paragraph title = new Paragraph("Order History", fontTitle);
      title.setAlignment(Element.ALIGN_CENTER);
      document.add(title);

      document.add(new Paragraph(" "));

      // Add table
      PdfPTable table = new PdfPTable(8); // Number of columns

      // Table Header
      Font headFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
      PdfPCell hcell;

      hcell = new PdfPCell(new Phrase("ID", headFont));
      table.addCell(hcell);

      hcell = new PdfPCell(new Phrase("Delivery Date", headFont));
      table.addCell(hcell);

      hcell = new PdfPCell(new Phrase("Vendor Name", headFont));
      table.addCell(hcell);

      hcell = new PdfPCell(new Phrase("Item Name", headFont));
      table.addCell(hcell);

      hcell = new PdfPCell(new Phrase("Size", headFont));
      table.addCell(hcell);

      hcell = new PdfPCell(new Phrase("Quantity", headFont));
      table.addCell(hcell);

      hcell = new PdfPCell(new Phrase("Amount", headFont));
      table.addCell(hcell);

      hcell = new PdfPCell(new Phrase("Discount", headFont));
      table.addCell(hcell);

      // Table Data
      for (OrderDetailsDTO order : orderHistory) {
        table.addCell(String.valueOf(order.getOrderId()));
        table.addCell(order.getDeliveryDate().toString());
        table.addCell(order.getVendorName());
        table.addCell(order.getItemName());
        table.addCell(order.getSize());
        table.addCell(String.valueOf(order.getQuantity()));
        table.addCell("" + order.getAmount());
        table.addCell("" + order.getDiscount());
      }
      document.add(table);
      document.close();
      byte[] pdfBytes = out.toByteArray();
      return new ByteArrayResource(pdfBytes);
    } catch (DocumentException e) {
      throw new RuntimeException("Failed to generate PDF", e);
    }
  }

  private List<OrderDetailsDTO> get(List<Order> orders) {
    if (orders == null || orders.isEmpty()) {
      return List.of();
    }
    var result =
        orders.stream()
            .map(
                order -> {
                  var skuId = order.getSubscription().getSkuId();
                  // PriceList priceList = serviceManager
                  //     .getPriceListService().fetchVendorSkuPrice(skuId);
                  // var sku = priceList.getSku();
                  var skuDto = serviceManager.getSkuService().fetchSkuById(skuId);
                  var vendor =
                      serviceManager
                          .getVendorService()
                          .fetchFullVendorDetailsById(order.getVendorId(), null);
                  return OrderDetailsDTO.builder()
                      .orderId(order.getId())
                      .deliveryDate(order.getDeliveryDate())
                      .size(skuDto.getSize())
                      .discount(BigDecimal.valueOf(0)) // TODO
                      .itemName(skuDto.getName())
                      .quantity(order.getQuantity())
                      .unitPrice(order.getPrice().doubleValue())
                      .amount(BigDecimal.valueOf(order.getQuantity()).multiply(order.getPrice()))
                      // .totalAmount(order.getTotalAmount()) //TODO
                      // .totalDiscount(0.0) //TODO
                      .vendorName(vendor.getBusinessName())
                      .build();
                })
            .toList();
    log.info("No of order for user {} ", result.size());
    return result;
  }
}
