import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenLocal()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    implementation("redis.clients:jedis:3.5.1")
    implementation("me.hyfe.helper:core:1.0.0")
}

tasks.processResources {
    expand("version" to project.version)
}

val shadowJar by tasks.getting(ShadowJar::class) {
    archiveFileName.set(project.parent!!.name + "-" + project.name + ".jar")
    relocate("redis.clients", "me.hyfe.queue.redis.clients")
    relocate("me.hyfe.helper", "me.hyfe.queue.helper")
}