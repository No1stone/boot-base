
plugins {
	kotlin("jvm") version "2.0.21"
	kotlin("plugin.spring") version "2.0.21"
//	id("org.springframework.boot")
//	id("io.spring.dependency-management")
	id("com.netflix.dgs.codegen") version "8.2.1"
}

kotlin {
	jvmToolchain(17)
}

group = "com.origemite"
version = "0.0.1-SNAPSHOT"
description = "batch-realtime"

dependencies {
	implementation(project(":lib-proto"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	implementation("org.slf4j:slf4j-api:2.0.12")

	//gRPC
	implementation ("org.springframework.grpc:spring-grpc-spring-boot-starter")
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
