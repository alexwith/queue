import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow")
}

group = "me.hyfe.queuenode"
version = "1.0.0"

repositories {
    mavenLocal()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    implementation("me.hyfe.helper:core:1.0.0")
}

tasks.processResources {
    expand("version" to project.version)
}

val shadowJar by tasks.getting(ShadowJar::class) {
    archiveFileName.set(project.name + ".jar")
    relocate("redis.clients", "me.hyfe.queuenode.redis.clients")
    relocate("me.hyfe.helper", "me.hyfe.queuenode.helper")
}