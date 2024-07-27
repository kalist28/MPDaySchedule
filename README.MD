# Day Schedule Compose Multiplatform

[![compose-mp-version](https://img.shields.io/badge/compose--multiplatform-1.6.11-blue)](https://github.com/JetBrains/compose-multiplatform)
[![kotlin-version](https://img.shields.io/badge/kotlin-2.0.0-blue)](https://github.com/JetBrains/kotlin)
[![Maven Central](https://img.shields.io/badge/alpha-0.0.1-blue)](https://repo1.maven.org/maven2/io/github/kalist28/dayschedule/)

![badge-Android](https://img.shields.io/badge/Platform-Android-brightgreen)
![badge-iOS](https://img.shields.io/badge/Platform-iOS-lightgray)
![badge-JVM](https://img.shields.io/badge/Platform-JVM-orange)
![badge-macOS](https://img.shields.io/badge/Platform-macOS-purple)
![badge-web](https://img.shields.io/badge/Platform-Web-blue)

The Kotlin Multiplatform library for the [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform).

<img src="https://github.com/kalist28/MPDaySchedule/blob/main/example/screen_1.png?raw=false" height="600" />

## Feature map
| Feature                    | State |
|----------------------------|:-----:|
| Base schedule              |   ✅   |
| All customization          |   ✅   |
| Several events on one line | Soon  |
| Layout strategy            | Soon  |
| Week schedule              | Soon  |

# Setup

[![Maven Central](https://img.shields.io/badge/alpha-0.0.1-blue)](https://repo1.maven.org/maven2/io/github/kalist28/dayschedule/)

In Android project: 

```groovy
dependencies {
  implementation 'io.github.kalist28:dayschedule:alpha-0.0.1'
}
```

In Kotlin Multiplatform project:

```groovy
commonMain.dependencies {
  implementation("io.github.kalist28:dayschedule:alpha-0.0.1")
}
```

```kotlin
val events = listOf(
    Event(
        LocalTime(hour = 1, minute = 0),
        LocalTime(hour = 2, minute = 0),
    ), 
    // or implement interface
    object : Event {
        override val from: LocalTime = LocalTime(hour = 1, minute = 0)
        override val to: LocalTime = LocalTime(hour = 2, minute = 0)
    }
)

DayScheduleLayout(
    events = events
) { event ->
    EventContainer(
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxSize(),
        markerColor = Color.Blue,
        containerColor = Color.Blue.copy(0.2f)
    ) {
        Text(
            event.run { listOf(from, to)
                .joinToString { it.toString() } },
            color = Color.White
        )
    }
}
```
