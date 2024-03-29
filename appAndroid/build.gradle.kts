import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
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
		minSdk = 29
		targetSdk = 34
		versionCode = 3
		versionName = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.8"
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {
	implementation(project(":appCommon"))

	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("com.google.android.material:material:1.11.0")

	implementation("androidx.activity:activity-compose:1.8.2")
	implementation(platform("androidx.compose:compose-bom:2022.10.00"))

	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.material:material")
	implementation("androidx.compose.runtime:runtime")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
}