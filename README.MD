# Coupon Service #

## Getting Started

### Prerequisites

- [Git](https://git-scm.com/)
- [Gradle](https://gradle.org/) (`sdk install gradle 3.1`)
- [lomok](https://projectlombok.org/) install idea plugin **lombok**
- [google-java-format](https://github.com/google/google-java-format) install idea **https://github.com/google/google-java-format**

### Tools

- style check with google java style
- integrate findbugs to quantity check
- integrate jacoco for test coverage
- integrate flyway to do migration
- integrate Orika to do object mapping
- use git properties to provide service info
- use spring boot builtin build info to provide service info
- integrate querydsl to enhance db query and custom repository factory bean to provide join fetch
- schedule with quartz
- jsr303 support
- lombok integrate
- Java SE8 Date & Time
- support query/path params validator


## Build

Run `gradle` for clean build

## Debug

1. Run `gradle bootRun` to start application
2. Attach **5005** to **remote** to fire a debug

## Note

1. Any class located in **base** or **common** will be move to **chainz-core** in future
2. Should cover unit tests
3. Should integrate authorization strategy in future
