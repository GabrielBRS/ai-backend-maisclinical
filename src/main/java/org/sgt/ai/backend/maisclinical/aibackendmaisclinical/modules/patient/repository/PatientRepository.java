package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.repository;

import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.AllergyRecord;
import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.PatientSummary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository {
    Optional<PatientSummary> findSummaryById(UUID id);
    List<AllergyRecord> findAllergiesByPatientId(UUID id);
}
