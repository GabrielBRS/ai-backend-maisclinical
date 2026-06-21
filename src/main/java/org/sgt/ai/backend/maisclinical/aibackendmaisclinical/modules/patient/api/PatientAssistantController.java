package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.api;

import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.ai.assistant.PatientAssistantService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients/{patientId}/assistant")
@RequiredArgsConstructor
public class PatientAssistantController {

    private final PatientAssistantService assistant;

    @PostMapping
    public AssistantResponse ask(@PathVariable UUID patientId,
                                 @RequestBody AssistantRequest req) {
        // Aqui entra sua verificação de autorização: o usuário pode ver ESTE patientId?
        String answer = assistant.ask(patientId, req.conversationId(), req.question());
        return new AssistantResponse(answer);
    }

    public record AssistantRequest(String conversationId, String question) {}
    public record AssistantResponse(String answer) {}
}

