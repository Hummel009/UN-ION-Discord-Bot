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

	implementation("androidx.activity:activity-compose:latest.release")
	implementation(platform("androidx.compose:compose-bom:latest.release"))

	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.material:material")
	implementation("androidx.compose.runtime:runtime")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(8)
	}
}