# Coupon Service #

## Getting Started

### Prerequisites

- [Git](https://git-scm.com/)
- [Gradle](https://gradle.org/) (`sdk install gradle 3.1`)
- [lomok](https://projectlombok.org/) install idea plugin **lombok**
- [google-java-format](https://github.com/google/google-java-format) install idea **https://github.com/google/google-java-format**


## Build

Run `gradle` for clean build

## Debug

1. Run `gradle bootRun` to start application
2. Attach to **remote** to fire a debug

## Hotreload

1. Ensure enabled **Annotation Processors**, located at Preferences... > Build, Execution, Deployment > Compiler > Annotation Processors
2. CMD+Shift+F9 to compile current class
3. Spring Boot Devtools will detect the modification and apply it
