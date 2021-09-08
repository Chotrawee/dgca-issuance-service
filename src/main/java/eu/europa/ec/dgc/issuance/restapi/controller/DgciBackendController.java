package eu.europa.ec.dgc.issuance.restapi.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import eu.europa.ec.dgc.issuance.DgcIssuanceApplication;
import eu.europa.ec.dgc.issuance.restapi.dto.EgdcCodeData;
import eu.europa.ec.dgc.issuance.service.DgciService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dgci")
@AllArgsConstructor
@ConditionalOnExpression("${issuance.endpoints.backendIssuing:false}")
public class DgciBackendController {
    private final DgciService dgciService;

    @Operation(
        summary = "create qr code of edgc",
        description = "create edgc for given data"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "signed edgc qr code created"),
        @ApiResponse(responseCode = "400", description = "wrong issue data")})
    @PutMapping(value = "/issue", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EgdcCodeData> createEdgc(@RequestBody String eudgc) {
        EgdcCodeData egdcCodeData = dgciService.createEdgc(eudgc);
        return ResponseEntity.ok(egdcCodeData);
    }


    @Operation(
        summary = "create qr code of edgc",
        description = "create edgc for given data"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "signed edgc qr code created"),
        @ApiResponse(responseCode = "400", description = "wrong issue data")})
    @PostMapping(value = "/qrGen",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
    private ResponseEntity<BufferedImage> generateQR(@RequestBody String eudgc)
        throws Exception {
        return ResponseEntity.ok(toQR(eudgc, 300, ErrorCorrectionLevel.L));
    }

    @Operation(
        summary = "create qr code of edgc",
        description = "create edgc for given data"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "signed edgc qr code created"),
        @ApiResponse(responseCode = "400", description = "wrong issue data")})
    @PostMapping(value = "/qrGen/{sa}/{ecl}",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
    private ResponseEntity<BufferedImage> generateQR(@RequestBody String eudgc,
                                                     @PathVariable int sa,
                                                     @PathVariable ErrorCorrectionLevel ecl)
        throws Exception {
        return ResponseEntity.ok(toQR(eudgc, sa, ecl));
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    private BufferedImage toQR(String eudgc, int sideArea, ErrorCorrectionLevel errorCorrectionLevel) throws Exception {
        EgdcCodeData egdcCodeData = dgciService.createEdgc(eudgc);
        String url = DgcIssuanceApplication.url + "/cert/verify/?" +
            URLEncoder.encode(egdcCodeData.getQrCode(), "UTF-8");
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        Map<EncodeHintType, ErrorCorrectionLevel> encodeHintTypeMap = new HashMap<>();
        encodeHintTypeMap.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);

        BitMatrix bitMatrix =
            barcodeWriter.encode(url, BarcodeFormat.QR_CODE, sideArea, sideArea, encodeHintTypeMap);


        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
