package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.repository;

import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.ActiveMedication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Profile("dev")
public class StubMedicationRepository implements MedicationRepository {

    @Override
    public List<ActiveMedication> findActiveByPatientId(UUID id) {
        return List.of(new ActiveMedication("Losartana", "50mg", "1x/dia"));
    }
}
