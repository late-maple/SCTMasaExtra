# SCTMasaExtra

Some extra tweaks for masa's mods, made for the SCT server.

## Features

- **creativeDisableBlockTypeBreakRestriction** : When enabled, tweakeroo's block type break restriction (blacklist/whitelist) no longer applies in creative mode. Breaking restriction and breaking grid keep working normally.
- **creativeSingleClickBreakProtect** : When enabled (together with tweakeroo's *Disable Block Break Cooldown*), a short left click in creative mode only breaks the targeted block instead of a whole line. Fast continuous breaking starts once the attack key has been held longer than **creativeBreakHoldThresholdTicks** (default 3 ticks / 150 ms, range 1-20).
- **openConfigGui** : Open the config GUI with `S, C` (configurable), or via Mod Menu. Config is stored in `config/sctmasaextra.json`.

## Dependencies

- [malilib](https://modrinth.com/mod/malilib) >= 0.29.3
- [tweakeroo](https://modrinth.com/mod/tweakeroo) >= 0.29.2
- [litematica](https://modrinth.com/mod/litematica) >= 0.28.4
- Java 25+

## Supported Minecraft Version

- 26.2

## Development

Issues and PRs are welcome.

Build with `./gradlew build` (JDK 25); the jar will be placed in `versions/26.2/build/libs/`.

## License

This project is available under the [LGPL-3.0](LICENSE) license.
