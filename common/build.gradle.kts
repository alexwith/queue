plugins {
    java
    id("com.github.johnrengelman.shadow")
}

repositories {

}

dependencies {
    implementation("me.hyfe.helper:core:1.0.0")
    implementation("redis.clients:jedis:3.5.1")
    implementation("org.yaml:snakeyaml:1.28");
}

tasks.processResources {
    expand("version" to project.version)
}