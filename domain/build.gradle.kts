plugins {
    java
}

dependencies {
    implementation(project(":api"))

    testCompileOnly(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.assertj)
    testImplementation(testFixtures(project(":common")))
}


tasks {
    test {
        useJUnitPlatform()
    }
    compileTestJava {
        options.compilerArgs.add("-parameters")
    }
}