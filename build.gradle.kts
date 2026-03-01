import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

buildscript {
    configurations.classpath {
        resolutionStrategy {
            // Loom 1.13.6 ships kotlin-metadata-jvm 2.0.21 which only knows
            // Kotlin metadata up to 2.2.0. Fabric API for 1.21.11 was compiled
            // with Kotlin 2.3.x, so we must force a newer version here.
            force("org.jetbrains.kotlin:kotlin-metadata-jvm:2.3.10")
        }
    }
}

plugins {
    id("fabric-loom") version "1.13.6"
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.10"
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

repositories {
    maven { url = uri("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") }
    maven { url = uri("https://repo.nea.moe/mirror") }
    maven { url = uri("https://repo.nea.moe/releases/") }
    maven { url = uri("https://maven.shedaniel.me/") }
    maven { url = uri("https://maven.terraformersmc.com/releases/") }
    maven { url = uri("https://jitpack.io/") }
    maven {
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("fabric_kotlin_version")}")
    modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:${project.property("devAuth_version")}")
    implementation("meteordevelopment:orbit:${project.property("orbit_version")}")
    include("meteordevelopment:orbit:${project.property("orbit_version")}")
    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.property("clothConfig_version")}")
    include("me.shedaniel.cloth:cloth-config-fabric:${project.property("clothConfig_version")}")
    modImplementation("moe.nea.jarvis:jarvis-fabric:${project.property("jarvis_version")}")
    include("moe.nea.jarvis:jarvis-fabric:${project.property("jarvis_version")}")
    modLocalRuntime("moe.nea.jarvis:jarvis-fabric:${project.property("jarvis_version")}")
    modImplementation("maven.modrinth:modmenu:${project.property("modMenu_version")}")
    modLocalRuntime("maven.modrinth:modmenu:${project.property("modMenu_version")}")
}

loom {
    runs {
        removeIf { it.name != "client" }
        named("client") {
            property("devauth.enabled", "true")
            property("fabric.log.level", "info")
            vmArg("-ea")
        }
    }
    accessWidenerPath.set(file("src/main/resources/dulkirmod.accesswidener"))
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${base.archivesName.get()}" }
    }
}
