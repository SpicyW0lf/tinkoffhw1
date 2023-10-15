plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("io.github.resilience4j:resilience4j-ratelimiter:2.1.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
