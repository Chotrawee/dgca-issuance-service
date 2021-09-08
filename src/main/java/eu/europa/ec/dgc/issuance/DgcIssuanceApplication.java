/*-
 * ---license-start
 * EU Digital Green Certificate Issuance Service / dgca-issuance-service
 * ---
 * Copyright (C) 2021 T-Systems International GmbH and all other contributors
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ---license-end
 */

package eu.europa.ec.dgc.issuance;


import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;
import ehn.techiop.hcert.kotlin.chain.Base45Service;
import ehn.techiop.hcert.kotlin.chain.impl.DefaultBase45Service;
import eu.europa.ec.dgc.issuance.config.IssuanceConfigProperties;
import eu.europa.ec.dgc.issuance.entity.DgciEntity;
import eu.europa.ec.dgc.issuance.entity.GreenCertificateType;
import eu.europa.ec.dgc.issuance.restapi.controller.DgciBackendController;
import eu.europa.ec.dgc.issuance.restapi.controller.DgciController;
import eu.europa.ec.dgc.issuance.restapi.dto.*;
import eu.europa.ec.dgc.issuance.service.DgciService;
import eu.europa.ec.dgc.issuance.service.EdgcValidator;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;


/**
 * The Application class.
 */
@SpringBootApplication
@EnableConfigurationProperties({IssuanceConfigProperties.class})
public class DgcIssuanceApplication extends SpringBootServletInitializer {

    public static String url = "http://localhost:8080";

    /**
     * The main Method.
     *
     * @param args the args for the main method
     */
    public static void main(String[] args) {
        SpringApplication.run(DgcIssuanceApplication.class, args);
        if(StringUtils.hasText(System.getProperty("hostname"))) {
            url = System.getProperty("hostname");
        }
    }

}
