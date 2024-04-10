plugins {
	id("org.jetbrains.kotlin.jvm")
}

dependencies {
	implementation("net.lingala.zip4j:zip4j:2.11.5")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
	implementation("org.javacord:javacord:3.8.0") {
		exclude(group = "org.bouncycastle")
	}
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(8)
	}
}