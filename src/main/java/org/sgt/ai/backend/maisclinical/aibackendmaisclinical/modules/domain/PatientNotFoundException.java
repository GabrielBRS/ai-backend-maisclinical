package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain;

import java.util.UUID;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(UUID id) { super("Paciente não encontrado: " + id); }
}
