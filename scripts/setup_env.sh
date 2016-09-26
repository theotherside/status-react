#!/usr/bin/env bash
brew update
brew cask install java
brew outdated ant || brew install ant
brew outdated maven || brew install maven
brew outdated gradle || brew install gradle
brew outdated android-sdk || brew install android-sdk
brew outdated android-ndk || brew install android-ndk
echo 'export ANT_HOME=/usr/local/opt/ant' >> ~/.bash_profile
echo 'export MAVEN_HOME=/usr/local/opt/maven' >> ~/.bash_profile
echo 'export GRADLE_HOME=/usr/local/opt/gradle' >> ~/.bash_profile
echo 'export ANDROID_HOME=/usr/local/opt/android-sdk' >> ~/.bash_profile
echo 'export ANDROID_NDK_HOME=/usr/local/opt/android-ndk' >> ~/.bash_profile
echo 'export PATH=$ANT_HOME/bin:$PATH' >> ~/.bash_profile
echo 'export PATH=$MAVEN_HOME/bin:$PATH' >> ~/.bash_profile
echo 'export PATH=$GRADLE_HOME/bin:$PATH' >> ~/.bash_profile
echo 'export PATH=$ANDROID_HOME/tools:$PATH' >> ~/.bash_profile
echo 'export PATH=$ANDROID_HOME/platform-tools:$PATH' >> ~/.bash_profile
echo 'export PATH=$ANDROID_HOME/build-tools/$(ls $ANDROID_HOME/build-tools | sort | tail -1):$PATH' >> ~/.bash_profile
( sleep 5 && while [ 1 ]; do sleep 1; echo y; done ) | android update sdk --no-https --no-ui
. ~/.bash_profile
brew outdated node || brew install node
brew outdated watchman || brew install watchman
brew outdated leiningen || brew install leiningen
npm install -g react-native-cli
npm install -g appium
lein deps && npm install && ./re-natal deps