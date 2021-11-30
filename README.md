# Magic: The Gathering Android Challenge

<img src="https://user-images.githubusercontent.com/47601553/144115779-9b0a6f80-5f71-43a3-98b7-e01acf74a0a3.png" width="500" style="max-width:100%;">

## Table of Contents

- [Prerequisite](#prerequisite)
- [The App](#theapp)
- [Architecture](#architecture)
- [Testing & Automation](#testing)
- [ScreenShots](#screenshots)
- [Sample App and Source Code](#sampleappandsourcecode)

## Prerequisite

This project uses the Gradle build system. To build this project, use the `fastlane debug_build`
`gradlew build` command after cloning or use "Import Project" in Android Studio.

## The App

A small app that loads and presents a list of `Magic: The Gathering` sets/cards from  [MTG API](https://magicthegathering.io/).
The app has one Activity namely MainActivity and three fragments which are connected using navigation graph.
MainActivity acts as entry point with a NavHostFragment which hold the three fragmnents; SetsFragment, CardsFragment and 
CardDetailsFragment. Basically the user can:

    Pick a set from a list and see the list of cards belonging to it;
    pick a card and see its details (name, colors, image, type, flavor and rarity).
    Get a random booster from a specific set and list the cards and see their details



The project has been written in Kotlin language. For network requests, it uses Retrofit with RxJava and Coroutines.

Dagger Hilt has been used for Dependency injection.

## Architecture
The project is built using the MVVM architectural pattern and make heavy use of a couple of Android Jetpack components. Mvvm allows for the separation of concern which also makes testing easier.


## MVVM implementation
The first time the app is opened, the data will be fetched, paged request using Paing 3 , from the backend api service and stored locally with 
the help of Room database.
But if there is no internet or the api service is down, the data will be fetched from the local cache.
This is handled in the repository and remotemediator classes.
ViewModel is basically responsible for updating the UI (Activity/Fragment) with the data changes.
The ViewModel will initialise an instance of the Repository class and update the UI based with this data.



## Testing
All tests are under the Android Test package. All the tests are run using JUnit.
To run tests using fastlane run `gradlew build` command in your CL.
Test automation have also been achieved using CircleCi.

## ScreenShots


The app is available in both day and night theme.


<img src="https://user-images.githubusercontent.com/47601553/144119212-f72b4b42-02bd-41d9-8826-eb8c671b1f6d.png" width="200" style="max-width:100%;">    <img src="https://user-images.githubusercontent.com/47601553/144119340-f519a9d0-f03b-45d3-9ea7-17e0bf2202a7.png" width="200" style="max-width:100%;">   <img src="https://user-images.githubusercontent.com/47601553/144119432-7cbdce23-e80f-4e49-ab6e-29d154c5facb.png" width="200" style="max-width:100%;">  <img src="https://user-images.githubusercontent.com/47601553/144119681-5807a6eb-6c74-453d-81a0-655e19cdc531.png" width="200" style="max-width:100%;"> </br></br>


Libraries used in the whole application are:

- [Viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Manage UI related data in a lifecycle conscious way 
- [RxJava](https://github.com/ReactiveX/RxJava) - RxJava is a Java VM implementation of Reactive Extensions: a library for composing asynchronous and event-based programs by using observable sequences.
- [Kotlin.coroutines](https://developer.android.com/kotlin/coroutines?gclid=Cj0KCQjw1dGJBhD4ARIsANb6Odld-9wkN4Lkm6UJAvWRshusopwstZH5IXkSLzxv_Q5JYjgjozIywfcaAlS9EALw_wcB&gclsrc=aw.ds) - Concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
- [Kotlin Flow](https://developer.android.com/kotlin/flow) -An asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Dagger Hilt](https://dagger.dev/hilt/) - Used for Dependency injection
    - To simplify Dagger-related infrastructure for Android apps.
    - To create a standard set of components and scopes to ease setup, readability/understanding, and code sharing between apps.
    - To provide an easy way to provision different bindings to various build types (e.g. testing, debug, or release).

- [Retrofit](https://square.github.io/retrofit/) - Turns your HTTP API into a Java interface.
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) -  Loading and displaying pages of data from a larger dataset from local storage or over network.
- [Fastlane](https://docs.fastlane.tools/getting-started/android/setup/) -  Automate beta deployments and releases for Android apps. 🚀
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html) - Enables mock creation, verification and stubbing for testing
- [CircleCi](https://circleci.com/continuous-integration/) - Achieving continuous integration
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) - A scriptable web server for testing HTTP clients
Kotlin coroutines
## Sample App and Source Code

Clone the project and run `fastlane debug_build` in terminal to generate app

[Source.Code](https://github.com/Hechio/magic-G) - Access to the project's github reporitory
