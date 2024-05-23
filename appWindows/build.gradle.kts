import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("org.jetbrains.kotlin.jvm")
	id("org.jetbrains.kotlin.plugin.compose")
	id("org.jetbrains.compose")
}

group = "com.github.hummel"
version = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

dependencies {
	implementation(project(":appCommon"))
	implementation(compose.desktop.currentOs)
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(8)
	}
}

compose {
	desktop {
		application {
			mainClass = "com.github.hummel.union.windows.MainKt"
		}
	}
}

tasks {
	jar {
		manifest {
			attributes(
				mapOf(
					"Main-Class" to "com.github.hummel.union.windows.MainKt"
				)
			)
		}
		from(configurations.runtimeClasspath.get().map {
			if (it.isDirectory) it else zipTree(it)
		})
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}
}
