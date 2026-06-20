plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "1.1.1"
}

group = "org.sgt.ai.backend.maisclinical"
version = "0.0.1-SNAPSHOT"
description = "ai-backend-maisclinical"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
    // Spring AI milestones/snapshots — necessário se a versão compatível com
    // Boot 4.1 ainda não for GA no Maven Central. Remova se usar versão GA.
    maven { url = uri("https://repo.spring.io/milestone") }
    // maven { url = uri("https://repo.spring.io/snapshot") }
}

// ===== BOM do Spring AI: alinha todas as versões das libs de IA entre si =====
// VALIDE a versão exata compatível com Spring Boot 4.1 em:
// https://docs.spring.io/spring-ai/reference/getting-started.html
extra["springAiVersion"] = "1.1.0"

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

dependencies {
    // ===== SUAS DEPENDÊNCIAS ORIGINAIS (intactas) =====
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testCompileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")

    // ===== IA — LLM + AGENTES (tool calling embutido) =====
    implementation("org.springframework.ai:spring-ai-starter-model-ollama")

    // ===== IA — RETRIEVAL (pgvector no .200) =====
//    implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")

    // ===== IA — RAG (advisors) =====
    implementation("org.springframework.ai:spring-ai-advisors-vector-store")

    // ===== IA — INGESTÃO DE DOCUMENTOS =====
    implementation("org.springframework.ai:spring-ai-pdf-document-reader")
    implementation("org.springframework.ai:spring-ai-tika-document-reader")

    // ===== IA — MEMÓRIA =====
    // REMOVIDO: spring-ai-starter-model-chat-memory-repository-jdbc
    //   → incompatível com Boot 4.1 (referencia OnDatabaseInitializationCondition,
    //     classe que mudou de lugar no Spring Boot 4).
    // Use InMemoryChatMemoryRepository (já no core) p/ dev, ou implemente um
    // ChatMemoryRepository custom contra Redis/Valkey no .200 p/ produção.
}

tasks.withType<Test> {
    useJUnitPlatform()
}