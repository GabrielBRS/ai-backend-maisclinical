package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.ai.assistant;

import org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.ai.tools.PatientClinicalTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;

@Service
public class PatientAssistantService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public PatientAssistantService(ChatClient.Builder builder,
                                   VectorStore vectorStore,
                                   ChatMemory chatMemory,
                                   PatientClinicalTools patientTools) {
        this.vectorStore = vectorStore;
        this.chatClient = builder
                .defaultSystem("""
                Você é um assistente de APOIO à decisão clínica do MaisClinical.
                Responda EXCLUSIVAMENTE com base (a) nos dados retornados pelas tools do
                paciente e (b) nos documentos recuperados pelo RAG. Se a informação não
                estiver disponível, declare isso explicitamente — JAMAIS invente dado clínico.
                Você não substitui o julgamento do profissional de saúde habilitado.
                """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(patientTools)   // o agente: tools sempre disponíveis
                .build();
    }

    public String ask(UUID patientId, String conversationId, String question) {

        // RAG modular com ISOLAMENTO por paciente
        var ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .similarityThreshold(0.55)
                        .topK(6)
                        .filterExpression(new FilterExpressionBuilder()
                                .eq("patientId", patientId.toString())
                                .build())                       // só docs deste paciente
                        .build())
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(false)           // sem contexto → não responde no escuro
                        .build())
                .build();

        return chatClient.prompt()
                .advisors(a -> a
                        .advisors(ragAdvisor)
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .toolContext(Map.of("patientId", patientId))  // tools leem o paciente DAQUI
                .user(question)
                .call()
                .content();
    }
}
