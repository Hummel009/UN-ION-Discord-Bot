pluginManagement {
	repositories {
		google()
		mavenLocal()
		mavenCentral()
		maven("https://androidx.dev/storage/compose-compiler/repository")
		gradlePluginPortal()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenLocal()
		mavenCentral()
		maven("https://androidx.dev/storage/compose-compiler/repository")
		gradlePluginPortal()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":appAndroid")
include(":appWindows")
include(":appCommon")
include(":appSpecial")
