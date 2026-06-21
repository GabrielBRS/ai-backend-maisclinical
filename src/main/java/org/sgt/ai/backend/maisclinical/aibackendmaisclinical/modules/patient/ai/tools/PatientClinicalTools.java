package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.ai.tools;

import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.ActiveMedication;
import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.AllergyRecord;
import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.PatientNotFoundException;
import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain.PatientSummary;
import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.repository.MedicationRepository;
import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.repository.PatientRepository;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PatientClinicalTools {

    private final PatientRepository patientRepository;
    private final MedicationRepository medicationRepository;

    private static UUID currentPatient(ToolContext ctx) {
        // injetado pelo servidor via .toolContext(...), NUNCA pelo modelo
        return (UUID) ctx.getContext().get("patientId");
    }

    @Tool(description = "Recupera o resumo clínico estruturado (demografia, condições ativas) do paciente do contexto atual.")
    public PatientSummary getPatientSummary(ToolContext ctx) {
        return patientRepository.findSummaryById(currentPatient(ctx))
                .orElseThrow(() -> new PatientNotFoundException(currentPatient(ctx)));
    }

    @Tool(description = "Lista os medicamentos em uso pelo paciente do contexto, com dose e posologia.")
    public List<ActiveMedication> getActiveMedications(ToolContext ctx) {
        return medicationRepository.findActiveByPatientId(currentPatient(ctx));
    }

    @Tool(description = "Lista alergias e reações adversas do paciente. Consulte SEMPRE antes de qualquer raciocínio sobre interações ou prescrição.")
    public List<AllergyRecord> getAllergies(ToolContext ctx) {
        return patientRepository.findAllergiesByPatientId(currentPatient(ctx));
    }
}