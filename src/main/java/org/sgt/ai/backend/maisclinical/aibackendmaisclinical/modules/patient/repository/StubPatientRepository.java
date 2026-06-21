package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.repository;

import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.AllergyRecord;
import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.PatientSummary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("dev")   // ⚠️ APENAS dev. Em prod, a impl real contra Spring Data JDBC.
public class StubPatientRepository implements PatientRepository {

    @Override
    public Optional<PatientSummary> findSummaryById(UUID id) {
        return Optional.of(new PatientSummary(id, "Paciente Teste", 54, List.of("HAS", "DM2")));
    }

    @Override
    public List<AllergyRecord> findAllergiesByPatientId(UUID id) {
        return List.of(new AllergyRecord("Dipirona", "Urticária", "Moderada"));
    }
}