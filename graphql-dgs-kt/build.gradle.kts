//plugins {
//	kotlin("jvm") version "2.2.21"
//	kotlin("plugin.spring") version "2.2.21"
//	id("org.springframework.boot") version "4.0.0"
//	id("io.spring.dependency-management") version "1.1.7"
//	id("com.netflix.dgs.codegen") version "7.0.3"
//}

plugins {
	kotlin("jvm") version "2.0.21"
	kotlin("plugin.spring") version "2.0.21"
//	id("org.springframework.boot")
//	id("io.spring.dependency-management")
	id("com.netflix.dgs.codegen") version "8.2.1"
}


group = "com.origemite"
version = "0.0.1-SNAPSHOT"
description = "graphql-dgs-kt"

//java {
//	toolchain {
//		languageVersion = JavaLanguageVersion.of(17)
//	}
//}


repositories {
	mavenCentral()
}

dependencies {
//	implementation(project(":lib-common"))
//	implementation(project(":lib-model"))
//	implementation(project(":lib-legacy"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-graphql")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.generateJava {
	schemaPaths.add("${projectDir}/src/main/resources/graphql-client")
	packageName = "com.origemite.graphqldgskt.codegen"
	generateClient = true
}

tasks.withType<Test> {
	useJUnitPlatform()
}
