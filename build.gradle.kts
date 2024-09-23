import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("maven-publish")
    id("java")
    id("jacoco")
    id("application")
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

val versionFromProperty = "${project.property("version")}"
val versionFromEnv: String? = System.getenv("VERSION")

version = versionFromEnv ?: versionFromProperty
group = "${project.property("group")}"

val targetJavaVersion = (project.property("jdk_version") as String).toInt()
val javaVersion = JavaVersion.toVersion(targetJavaVersion)

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

repositories {
    mavenCentral()
    maven(url = "https://nexus.sibmaks.ru/repository/maven-snapshots/")
    maven(url = "https://nexus.sibmaks.ru/repository/maven-releases/")
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-core")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("org.flywaydb:flyway-core")
    implementation("com.h2database:h2")

    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.apache.commons:commons-lang3")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api")

    implementation("com.github.simple-mocks:api-common:${project.property("lib_api_common_version")}")
    implementation("com.github.simple-mocks:api-content:${project.property("lib_api_content_version")}")
    implementation("com.github.simple-mocks:api-error:${project.property("lib_api_error_version")}")
    implementation("com.github.simple-mocks:api-localization:${project.property("lib_api_localization_version")}")
    implementation("com.github.simple-mocks:api-session:${project.property("lib_api_session_version")}")
    implementation("com.github.simple-mocks:api-storage:${project.property("lib_api_storage_version")}")
    implementation("com.github.simple-mocks:api-async:${project.property("lib_api_async_version")}")
    implementation("com.github.simple-mocks:api-web-app:${project.property("lib_api_web_app_version")}")

    implementation("com.github.simple-mocks:service-content-embedded:${project.property("lib_embedded_content_version")}")
    implementation("com.github.simple-mocks:service-error-embedded:${project.property("lib_embedded_error_version")}")
    implementation("com.github.simple-mocks:service-localization-embedded:${project.property("lib_embedded_localization_version")}")
    implementation("com.github.simple-mocks:service-session-embedded:${project.property("lib_embedded_session_version")}")
    implementation("com.github.simple-mocks:service-storage-embedded:${project.property("lib_embedded_storage_version")}")
    implementation("com.github.simple-mocks:service-async-embedded:${project.property("lib_embedded_async_version")}")

    implementation("com.github.simple-mocks.web.app:web-app-base64:${project.property("lib_web_app_base64_version")}")
    implementation("com.github.simple-mocks.web.app:web-app-jolt:${project.property("lib_web_app_jolt_version")}")
    implementation("com.github.simple-mocks.web.app:web-app-mocks:${project.property("lib_web_app_mocks_version")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    testImplementation("org.mockito:mockito-core")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release = targetJavaVersion
    }
}

java {
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

tasks.register<Copy>("copyFrontendResources") {
    group = "build"
    description = "Copies the frontend build resources to the Spring Boot static directory"

    dependsOn(":web-app-frontend:buildFrontend")

    from(project(":web-app-frontend").file("build/out"))
    into(layout.buildDirectory.dir("resources/main/web/app/hub/static"))
}

tasks.named("processResources") {
    dependsOn("copyFrontendResources")
}

tasks.jar {
    dependsOn("copyFrontendResources")
    from("LICENSE") {
        rename { "${it}_${project.property("project_name")}" }
    }
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to project.name,
                "Specification-Vendor" to project.property("author"),
                "Specification-Version" to project.version,
                "Specification-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
                "Timestamp" to System.currentTimeMillis(),
                "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})"
            )
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("bootJava") {
            artifact(tasks.named("bootJar"))
            pom {
                packaging = "jar"
                url = "https://github.com/sibdevtools/web-app-hub"

                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://www.mit.edu/~amini/LICENSE.md")
                    }
                }

                scm {
                    connection.set("scm:https://github.com/sibdevtools/web-app-hub.git")
                    developerConnection.set("scm:git:ssh://github.com/sibdevtools")
                    url.set("https://github.com/sibdevtools/web-app-hub")
                }

                developers {
                    developer {
                        id.set("sibmaks")
                        name.set("Maksim Drobyshev")
                        email.set("sibmaks@vk.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releasesUrl = uri("https://nexus.sibmaks.ru/repository/maven-releases/")
            val snapshotsUrl = uri("https://nexus.sibmaks.ru/repository/maven-snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = project.findProperty("nexus_username")?.toString() ?: System.getenv("NEXUS_USERNAME")
                password = project.findProperty("nexus_password")?.toString() ?: System.getenv("NEXUS_PASSWORD")
            }
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sibdevtools/web-app-hub")
            credentials {
                username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
