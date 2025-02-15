# simple-mobile-vm

Written by Pete Procopio

## Simple VM Introduction

Simple machine language virtual machine. A small but powerful language to execute code.

Based on the article ["Programming Project: Building a Computer in Software:"](https://archive.org/details/byte-magazine-1985-10/page/n111/mode/2up) by Jonathan Amsterdam in [BYTE Magazine](https://archive.org/details/byte-magazine-1985-10) circa Oct. 1985.  Pg. 112

Written in ~~Java~~ Kotlin to run on Android.

Note: To open the Android project it is located in the simpleMobileVMApp directory

## Details

  * More details available on [wiki](https://github.com/ukonpete/simple-mobile-vm/wiki)

## Release Notes

* 2/12/2025 - 2/15/2025
  * Update for newer Android Studio and Gradle
  * Long overdue fix of timing issue with async code
  * Code cleanup
    * TODO
      * remove Rxjava
      * Use suspend functions and Flows
      * More refactoring to make the code more manageable
* 8/23/2020
  * Added RxJava/Android version of the VM library. It passes all unit tests, but it is not integrated with the app yet.
  * Upgrade to Gralde 7.0.1
  * Use new Kotlin Formatter
  * Use Kotlin better in places
* 8/23/2020
  * Upgrade to Kotlin
  * Upgrade to AndroidX
  * Upgrade to ConstrinatLayout
  * Upgrade to View Binding
* 1/5/2019
  * Code Cleanup
  * Updated Read Me
  * Updated App Icon
  * Updated License

## Javadoc

See javadoc directory in the project source directory under /simple-mobile-vm/simpleMobileVMApp/javadoc/

## 3rd Party Credits

No app is deployed in the app store, so placing credit here.

* [Icon8](https://icons8.com/license) - Icons used in App
* [Google](www.google.com) - Android, support-annotations, test runner, test rules
* [PMD](https://pmd.github.io/) - InfoEther, LLC
* [Gradle](https://gradle.org/) - Gradle Inc.
* [Android Studio](https://developer.android.com/studio/?gclid=CjwKCAiAyMHhBRBIEiwAkGN6fMw5VSOxZzOuKjTaUpdRIHbhV7InKbxFNtFP0_9RazAkBepJY4CFYRoCKY4QAvD_BwE) - IntelliJ and Google

## Project History

For those who want to see old change history and other stuff no longer used go the [Project History Wiki page](https://github.com/ukonpete/simple-mobile-vm/wiki/Project-History)

## License

    Copyright 2018 - 2025 Pete Procopio

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
	
