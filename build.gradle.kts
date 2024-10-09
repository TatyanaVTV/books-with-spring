plugins {
    id("java")
}

group = "ru.vtvhw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.springframework:spring-test:6.1.13")

    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.3.4")

    implementation("org.springframework:spring-jdbc:6.1.13")
    implementation("org.apache.commons:commons-dbcp2:2.12.0")
    implementation("org.postgresql:postgresql:42.7.4")
}

tasks.test {
    useJUnitPlatform()
}