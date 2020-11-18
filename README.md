# Overview

I'm using `Room` for Database, `ViewModel` and `LiveData` to hold and manage the screen state.
Each Activity/Fragment is observing a LiveData object from the `ViewModel` for changes
and reacts accordingly.

The View State is modelled as a Kotlin Sealed class. Screen sends Intents (Actions) 
to the ViewModel, ViewModel handles them by interacting with a `Repository` class and updates
the state. Then the View reacts to the updated state by re-rendering the screen.

The app contains unit tests for the `Repository` class and for some `ViewModel` methods.
I was planning to add integration tests using MockWebServer which is great for serving
fake responses but I wasn't able to set it up correctly. The code is in `androidTest` folder
and I'm using `Hilt` to uninstall dependencies from `DatabaseModule` and `NetworkModule` and
replace them with an in memory DB that allows queries to run on the main thread and 
`Schedulers.trampoline()` for the same reason. 


## Future improvements
1. Correctly set up instrumentation tests with mockWebServer.
2. Add generic interfaces for View (with a render() method) and ViewModel (with a state member).
3. Move business logic to separate classes for re-usability.
4. Mappers between domain models and ui models. (currently there are only used to map from/to ui models/data)
5. Use `buildSrc` to manage project dependencies.
6. Coroutines?
