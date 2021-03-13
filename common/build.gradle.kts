plugins {
    java
    id("com.github.johnrengelman.shadow")
}

repositories {

}

dependencies {
    implementation("redis.clients:jedis:3.5.1")
    implementation("org.yaml:snakeyaml:1.28");
}

tasks.processResources {
    expand("version" to project.version)
}