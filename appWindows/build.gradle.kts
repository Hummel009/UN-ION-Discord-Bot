import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("org.jetbrains.kotlin.jvm")
	//noinspection JavaPluginLanguageLevel
	id("application")
}

group = "hummel"
version = "v" + LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

val embed: Configuration by configurations.creating

dependencies {
	embed("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
	embed("com.google.code.gson:gson:2.10.1")
	embed("org.apache.httpcomponents.client5:httpclient5:5.3")
	embed("net.lingala.zip4j:zip4j:2.11.5")
	embed("org.javacord:javacord:3.8.0") {
		exclude(group = "org.bouncycastle")
	}
	implementation("net.lingala.zip4j:zip4j:2.11.5")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.3")
	implementation("org.javacord:javacord:3.8.0") {
		exclude(group = "org.bouncycastle")
	}
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
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
