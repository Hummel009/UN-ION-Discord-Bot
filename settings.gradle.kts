pluginManagement {
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":appAndroid")
include(":appWindows")
include(":appCommon")
