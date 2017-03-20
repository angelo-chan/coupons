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
- integrate springfox api documentation

## Credential

1. coupons service not integrate any account system or authorization strategy like OAuth, it can be extended simply by defining some filter.
2. at current, it simply extract operator information from specific headers

  * account type definitions: SYSTEM, VENDOR, STORE, CLIENT

  * | Account_Type | Vendor_Id | Store_Id | Open_Id |
    |--------------|-----------|----------|---------|
    |    SYSTEM    |   N/A     |    N/A   |    N/A  |
    |    VENDOR    | REQUIRED  |    N/A   |    N/A  |
    |    STORE     | REQUIRED  | REQUIRED |    N/A  |
    |    CLIENT    |   N/A     |    N/A   | REQUIRED|


## Doc

Visit http://localhost:8080/swagger-ui.html


## Build

Run `gradle` for clean build

## Debug

1. Run `gradle bootRun` to start application
2. Attach **5005** to **remote** to fire a debug

## Live-Reload

1. Run `gradle bootRun` to start application also it will trigger spring boot devTools
2. Code in idea and press CMD+Shift+F9 to compile current class

## Note

1. Please change to public repository before build
2. Any class located in **base** or **common** will be move to **chainz-core** in future
3. Should cover unit tests
4. Should integrate authorization strategy in future
