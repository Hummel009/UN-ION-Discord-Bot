import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.compose")
	id("org.jetbrains.compose")
}

android {
	namespace = "com.github.hummel.union"
	compileSdk = 34

	packaging {
		resources {
			excludes.add("META-INF/*")
		}
	}

	defaultConfig {
		applicationId = "com.github.hummel.union"
		minSdk = 34
		targetSdk = 34
		versionName = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))
	}
}

dependencies {
	implementation(project(":appCommon"))

	implementation("com.google.android.material:material:latest.release")

	val composeBom = platform("androidx.compose:compose-bom:latest.release")
	implementation(composeBom)
	implementation("androidx.compose.material3:material3")
	implementation("androidx.activity:activity-compose")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(8)
	}
}