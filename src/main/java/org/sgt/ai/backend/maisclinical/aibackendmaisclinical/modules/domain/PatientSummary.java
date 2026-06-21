package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.domain;

import java.util.List;
import java.util.UUID;

public record PatientSummary(UUID id, String fullName, int ageYears, List<String> activeConditions) {}

