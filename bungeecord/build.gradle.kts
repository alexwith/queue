import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {
    implementation(project(":common"))
    compileOnly("net.md-5:bungeecord-api:1.16-R0.5-SNAPSHOT")
}

tasks.processResources {
    expand("version" to project.version)
}

val shadowJar by tasks.getting(ShadowJar::class) {
    archiveFileName.set(project.parent!!.name + "-" + project.name + ".jar")
}