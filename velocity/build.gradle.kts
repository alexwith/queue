import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven { url = uri("https://nexus.velocitypowered.com/repository/maven-public/") }
}

dependencies {
    implementation(project(":common"))
    compileOnly("com.velocitypowered:velocity-api:1.1.4")
    annotationProcessor("com.velocitypowered:velocity-api:1.1.4")
}

tasks.processResources {
    expand("version" to project.version)
}

val shadowJar by tasks.getting(ShadowJar::class) {
    archiveFileName.set(project.parent!!.name + "-" + project.name + ".jar")
}