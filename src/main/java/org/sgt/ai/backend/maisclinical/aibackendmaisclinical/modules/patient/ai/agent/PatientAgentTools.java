package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.ai.agent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import java.util.function.Function;

@Configuration
public class PatientAgentTools {

    public record VitalSignsRequest(String patientId) {}
    public record VitalSignsResponse(String bloodPressure, int heartRate, double temperature) {}

    /**
     * Esta função será mapeada como uma Tool.
     * O @Description é VITAL: é ele que diz ao Ollama/LLM *quando* usar essa ferramenta.
     */
    @Bean
    @Description("Busca os sinais vitais atuais de um paciente no sistema de monitoramento em tempo real.")
    public Function<VitalSignsRequest, VitalSignsResponse> fetchCurrentVitals() {
        return request -> {
            // Em um cenário real, aqui entraria a lógica de busca no PostgreSQL via JDBC/JPA
            // ou uma requisição gRPC/REST para um serviço de telemetria.
            System.out.println("🔧 [TOOL EXECUTADA] Buscando vitais para o paciente: " + request.patientId());
            return new VitalSignsResponse("120/80", 75, 36.5);
        };
    }
}