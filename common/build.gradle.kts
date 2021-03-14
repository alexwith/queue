import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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

val shadowJar by tasks.getting(ShadowJar::class) {
    relocate("redis.clients", "me.hyfe.queue.redis.clients")
    relocate("org.yaml", "me.hyfe.queue.yaml")
}