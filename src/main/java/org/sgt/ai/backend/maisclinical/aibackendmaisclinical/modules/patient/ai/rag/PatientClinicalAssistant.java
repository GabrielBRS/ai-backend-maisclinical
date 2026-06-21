package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.ai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class PatientClinicalAssistant {

    private final ChatClient chatClient;

    public PatientClinicalAssistant(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore) {

        // Inicializamos a memória em tempo de execução para manter o contexto do diálogo
        ChatMemory chatMemory = new InMemoryChatMemory();

        this.chatClient = chatClientBuilder
                .defaultSystem("""
                    Você é um assistente clínico de IA avançado.
                    Seu objetivo é cruzar o histórico médico do paciente (fornecido via contexto)
                    com os dados vitais atuais para fornecer análises precisas.
                    Sempre aja com rigor científico e segurança em saúde.
                    """)
                // 1. Injetando RAG: O Advisor fará a busca vetorial antes de chamar o LLM
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(
                                vectorStore,
                                SearchRequest.builder().topK(3).build() // Busca os 3 fragmentos mais relevantes
                        ),
                        // 2. Injetando Memória
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                // 3. Injetando o Agente (Tools): O nome da string deve ser o nome do @Bean
                .defaultFunctions("fetchCurrentVitals")
                .build();
    }

    /**
     * Processa a pergunta do médico ou sistema sobre um paciente.
     */
    public String analyzePatientStatus(String chatId, String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                // Usamos o chatId para isolar a memória de diferentes conversas/pacientes
                .advisors(a -> a.param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .call()
                .content();
    }
}
