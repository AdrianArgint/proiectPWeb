package com.proiect.app.service;

import com.proiect.app.domain.Alert;
import com.proiect.app.repository.AlertRepository;
import com.proiect.app.service.dto.AlertDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlertService {

    private final Logger log = LoggerFactory.getLogger(PostService.class);
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /**
     * Save an alert.
     *
     * @param alertDTO the entity to save.
     * @return the persisted entity.
     */
    public void save(AlertDTO alertDTO) {
        log.debug("Request to save Alert : {}", alertDTO);
        Alert alert = new Alert();
        alert.setContent(alertDTO.getContent());
        alert.setTitle(alertDTO.getTitle());
        alertRepository.save(alert);
    }

    public Page<AlertDTO> findAll(Pageable pageable) {
        return alertRepository.findAll(pageable).map(alert -> new AlertDTO(alert.getId(), alert.getTitle(), alert.getContent()));
    }
}
