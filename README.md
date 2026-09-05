# SCTMasaExtra

A Fabric mod for the SCT server, based on [fallen's fabric-mod-template](https://github.com/Fallen-Breath/fabric-mod-template).

- Mod ID: `sctmasaextra`
- Author: late_maple
- Minecraft: 26.2 only

## Build

Requires **JDK 25** (MC 26.2 targets Java 25). If your `JAVA_HOME` points elsewhere:

```bash
JAVA_HOME=D:\env\jdk25 ./gradlew build
```

The built jar will be placed in `versions/26.2/build/libs/`.

## Development

- Java sources live in `src/main/java/top/sctserver/sctmasaextra`
- Mixin config: `src/main/resources/sctmasaextra.mixins.json`
- To add another Minecraft version later, create a folder under `versions/`, add it to `settings.json` and register a preprocessor node in `build.gradle`.
