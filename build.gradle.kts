plugins {
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("plugin.jpa") version "1.9.24"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "com.kotlinspring"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {

	// web
	implementation("org.springframework.boot:spring-boot-starter-web")

	// validator
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// logging
	implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")

	// db
	runtimeOnly("com.h2database:h2") // in-memory DB, meant to be used only for early development steps
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	runtimeOnly("org.postgresql:postgresql")

	// testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("io.mockk:mockk:1.10.4")
	testImplementation("com.ninja-squad:springmockk:3.0.1")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sourceSets { // lambda
	test { // lambda
		java {
			setSrcDirs(listOf("src/test/intg", "src/test/unit"))
		}
	}
}