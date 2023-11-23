import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("org.jetbrains.kotlin.jvm") version "1.9.20"
	id("application")
}

group = "hummel"
version = "v" + LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

repositories {
	mavenCentral()
}

val embed: Configuration by configurations.creating

dependencies {
	embed("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
	embed("com.google.code.gson:gson:2.10.1")
	embed("org.apache.httpcomponents:httpclient:4.5.14")
	embed("org.javacord:javacord:3.8.0") {
		exclude(group = "org.bouncycastle")
	}
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("org.apache.httpcomponents:httpclient:4.5.14")
	implementation("org.javacord:javacord:3.8.0") {
		exclude(group = "org.bouncycastle")
	}
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

application {
	mainClass = "hummel.MainKt"
}

tasks {
	named<JavaExec>("run") {
		standardInput = System.`in`
	}
	jar {
		manifest {
			attributes(
				mapOf(
					"Main-Class" to "hummel.MainKt"
				)
			)
		}
		from(embed.map {
			if (it.isDirectory) it else zipTree(it)
		})
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}
}
