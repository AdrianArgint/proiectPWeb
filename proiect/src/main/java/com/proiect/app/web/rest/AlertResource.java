package com.proiect.app.web.rest;

import com.proiect.app.service.AlertService;
import com.proiect.app.service.dto.AlertDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class AlertResource {

    private final Logger log = LoggerFactory.getLogger(PlaceResource.class);

    private final AlertService alertService;

    public AlertResource(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * {@code GET  /places} : get all the places.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of places in body.
     */
    @GetMapping("/alert")
    public ResponseEntity<List<AlertDTO>> getAllAlerts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Alerts");
        Page<AlertDTO> page = alertService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
