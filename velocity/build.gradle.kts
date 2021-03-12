plugins {
    java
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven { url = uri("https://nexus.velocitypowered.com/repository/maven-public/") }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:1.1.4")
}

tasks.processResources {
    expand("version" to project.version)
}