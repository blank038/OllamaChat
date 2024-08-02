plugins {
    id("java")
    kotlin("jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.blank038.ollamachat"
version = "1.0.0"

repositories {
    maven {
        name = "AiYo Studio Repository"
        url = uri("https://repo.mc9y.com/snapshots")
    }
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.aystudio.core:AyCore:1.2.0-BETA")
}

tasks {
    processResources {
        filesMatching("**/plugin.yml") {
            expand("version" to project.version)
        }
    }
    shadowJar {
        archiveFileName = "OllamaChat-${version}.jar"
        relocate("kotlin", "com.aiyostudio.kotlin")
        dependencies {
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib:1.9.21"))
        }
    }
}

kotlin {
    jvmToolchain(8)
}