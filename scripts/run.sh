#!/usr/bin/env bash
echo "ANDROID_HOME:  $ANDROID_HOME"
./scripts/figwheel_background.sh
./scripts/reactnative_background.sh
./scripts/appium_background.sh
adb reverse tcp:8081 tcp:8081
adb reverse tcp:3449 tcp:3449
react-native run-android
lein test
lein doo node test once