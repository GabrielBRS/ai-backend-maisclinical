package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.repository;

import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.ActiveMedication;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicationRepository {
    List<ActiveMedication> findActiveByPatientId(UUID id);
}
