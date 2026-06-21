package org.sgt.ai.backend.maisclinical.aibackendmaisclinical.modules.patient.ai.document_ingestion;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicalDocumentIngestionService {

    private final VectorStore vectorStore;

    public void ingestPatientDocument(UUID patientId, Resource pdf, String docType) {
        List<Document> chunks = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(350)
                .build()
                .apply(new PagePdfDocumentReader(pdf).get());

        chunks.forEach(d -> {
            d.getMetadata().put("patientId", patientId.toString());
            d.getMetadata().put("docType", docType);          // ex.: "exame_laboratorial", "evolucao"
            d.getMetadata().put("ingestedAt", Instant.now().toString());
        });

        vectorStore.add(chunks);
    }
}
