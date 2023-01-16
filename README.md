<div align="center">

  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://sublab.dev/logo_light.png">
    <img alt="Sublab logo" src="https://sublab.dev/logo.png">
  </picture>

</div>

[![Maven Central](https://img.shields.io/maven-central/v/dev.sublab/common-kotlin)](https://mvnrepository.com/artifact/dev.sublab/common-kotlin)
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Telegram channel](https://img.shields.io/badge/chat-telegram-green.svg?logo=telegram)](https://t.me/sublabsupport)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# Sublab's Common Kotlin library

This is a Kotlin repository which implements [Parity's SCALE codec](https://docs.substrate.io/reference/scale-codec/) in a designated way. 

This repository aims to provide easy to use interface similar to other serialization implementations such as JSON in multiple variants. 

It attemps to use Kotlin's reflection to minimize developer's customization in order to make particular data object to be ready for SCALE codec serialization.

Unfortunately, Kotlin is not almighty even with its reflection, and it misses fixed size arrays comparing to Java, so we try to use annotations and other small hacks to overcome these issues.

## Sublab

At Sublab we're making mobile-first libraries for developers in [Substrate](https://substrate.io) ecosystem. However, we seek our libraries to be available not only for mobile Apple OS or Android OS, but compatible with any Swift/Kotlin environment: web servers, desktop apps, and whatnot.

Our mission is to to develop fully native open-source libraries for mobile platforms in Polkadot and Kusama ecosystems, covering everything with reliable unit-tests and providing rich documentation to the developers community. 

Our goal is to have more developers to come into the world of development of client applications in Substrate ecosystem, as we find this as most promising and intelligent blockchain project we ever seen. Thus, we as mobile development gurus trying to create enormously professional libraries which might be very complicated under the hood, but very simple and convenient for final developers.

## Installation

This library is available at Maven Central. Feel free to copy and paste code blocks below to integrate it to your application.

**build.gradle**

```groovy
repositories {
    mavenCentral()
}
```

**Maven**

```xml
<dependency>
    <groupId>dev.sublab</groupId>
    <artifactId>scale-codec-kotlin</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Gradle**

```groovy
implementation 'dev.sublab:scale-codec-kotlin:1.0.0'
```

## Documentation

- Our GitBook: [https://docs.sublab.dev/kotlin-libraries/scale-codec-kotlin/](https://docs.sublab.dev/kotlin-libraries/scale-codec-kotlin/)
- API reference: [https://api-reference.sublab.dev/scale-codec-kotlin/](https://api-reference.sublab.dev/scale-codec-kotlin/)

## Contributing

Please look into our [contribution guide](CONTRIBUTING.md) and [code of conduct](CODE_OF_CONDUCT.md) prior to contributing.

## Contacts

- Website: [sublab.dev](https://sublab.dev)
- E-mail: [info@sublab.dev](mailto:info@sublab.dev)
- Telegram support channel: [t.me/sublabsupport](t.me/sublabsupport)
- Twitter: [twitter.com/sublabdev](https://twitter.com/sublabdev)
