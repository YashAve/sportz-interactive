#!/bin/bash
./gradlew uninstallDebug
./gradlew installDebug
adb shell am start -n com.performance.sportzinteractive/.ui.activity.MainActivity
