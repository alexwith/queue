plugins {
    java
}

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:6.1.0")
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

    group = "me.hyfe.queue"
    version = "1.0.0"

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        implementation("org.jetbrains:annotations:16.0.2")
        implementation("redis.clients:jedis:3.5.1")
        compileOnly("com.google.code.gson:gson:2.8.6")
    }
}
