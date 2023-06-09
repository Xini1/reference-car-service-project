plugins {
    java
}

dependencies {
    implementation(project(":api"))
    implementation(libs.logback)

    testCompileOnly(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.assertj)
}

tasks {
    test {
        useJUnitPlatform()
    }
}