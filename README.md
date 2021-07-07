# 📝 ConfigValues

[![](https://jitpack.io/v/community.leaf/configvalues.svg)](https://jitpack.io/#community.leaf/configvalues "Get maven artifacts on JitPack")
[![](https://img.shields.io/badge/License-MPL--2.0-blue)](./LICENSE "Project License: MPL-2.0")
[![](https://img.shields.io/badge/Java-11-orange)](#java-version "Java Version: 11")
[![](https://img.shields.io/badge/View-Javadocs-%234D7A97)](https://javadoc.jitpack.io/community/leaf/configvalues/config-values-parent/latest/javadoc/ "View Javadocs")

## Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>community.leaf.configvalues</groupId>
    <artifactId>config-values-bukkit</artifactId>
    <version><!--release--></version>
</dependency>
```

### Versions

Since we use JitPack to distribute this library, the versions available 
are the same as the `tags` found on the **releases page** of this repository.

### Shading

If you intend to shade this library, please consider **relocating** the packages
to avoid potential conflicts with other projects. This library also utilizes
nullness annotations, which may be undesirable in a shaded uber-jar. They can
safely be excluded, and you are encouraged to do so.

