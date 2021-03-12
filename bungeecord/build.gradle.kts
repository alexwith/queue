plugins {
    java
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.16-R0.5-SNAPSHOT")
}

tasks.processResources {
    expand("version" to project.version)
}