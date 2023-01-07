## General Testing Recommendations
After playing with the app a bit, these are general testing recommendations (in roughly priority order):
* Unit test any utility classes
* Unit test viewmodels or use cases (wherever the business logic lives)
* Unit test any repositories with a mock web server so you can test network error handling
* Add test coverage to identify which parts of code need additional testing
* Test each composable for display/click behavior (Compose tests are instrumentation tests)
* Robolectric to test code that uses Android-specific code
* Add smoke tests that can also be used for baseline profiles (automation/instrumentation tests) using fake network data for reliability
* Screenshot testing (unit tests) to check for layout changes if you upgrade libraries
* Add smoke tests with live server data so there's a canary job that can flag if the backend has broken the clients; 
alternatively, add Pact testing to the backend to make sure the client data formatting doesn't get broken
* Switched to RootCoverage plugin instead of using Kover; shows 46% code coverage

These are roughly in priority order and are generally in the testing pyramid shape.  There are minimal
tests for automation/integration tests.  The majority of tests are unit tests that don't require devices
because they are faster to run.

## Analysis of Codebase

Organization of codebase is decent.  The UI folder organizes functionality by feature.
The rest of the functionality in the data, di, service, util folders could be under lib
or core layer.  The networking data and service folders can go into a network layer;
some network stuff ended up in the util/interceptor folder that should be moved.

For better build speed, each of the UI folders can be broken up into separate modules.

Unit tests are fairly well done using mockk.
There are domain and network models for the data and the mappers are all tested.
Viewmodels and datastores and even the Paging source are all tested using the 
Kotlin 1.7.x coroutine test framework.  Some mappers are tested using manually
created classes instead of typical JSON from the server (MovieDetail appears to
be tested using JSON responses).

Android tests are also done well with what looks like all composables are tested
except for the main movies screen.  Unfortunately, it looks like they are out of
date with the current library versions and a few don't compile properly (e.g.
Insets are in Jetpack libraries instead of Accompanist).  Fixed the compile issues,
but the cast/crew tests can't seem to find the Cast/Crew tags.

A few types of testing are missing:
* high level instrumentation testing of the app on an emulator; this is generally done for smoke testing of the app
* network JSON testing of some of the data mappers for unit tests
* mock web server testing in unit tests for the data sources
* screenshot testing to verify nothing has changed with layouts when upgrading libraries

## Instrumentation Tests with Network Traffic

Because these hit the server, these tests are generally more fragile than regular tests
because the server can change the data it sends back to us.  This can be mitigated somewhat
by checking for null fields in your response data models, but if field changes from a
collection to a non-collection, client code will still break.

To avoid the network data instability, what is typically done is using a mock web server
and pointing the client app at the mock web server which has fixed JSON responses.  The
main difficulty with this is that the stored JSON responses no longer match what comes from the
server.  The JSON responses have to be manually saved into the codebase when the backend data changes
and sometimes need to be modified (e.g. to return only a single page of results).
Errors can be tested using mock web servers easily.

Another way to avoid the network instability is to use a customized okhttp interceptor to
return data specified in a file.  An alternative interceptor can be used to capture the
data into a file, so this can be done by running the tests in a "capture" mode.  
An AirBnB tool named okreplay provides this alternative but it is unfortunately no longer maintained.
This can't test network error states that well unless you can cause the backend to send error
states back easily because you usually record good responses.

## Changes
* Fixed compile errors with androidTest; had to disable two tests
* Updates Compose Libs to latest
* Removed debug manifest and used compose ui-test-manifest artifact instead
* Added MainActivityTest to androidTest
* Added missing hilt testing and navigation libs and set up hilt for androidTest
* Added MoviesGrid and MoviesContent test tags
* Added okhttp mockwebserver lib
* Added okhttp logging lib to see errors in network traffic
* Added android:usesCleartextTraffic="true" to manifest for http://127.0.0.1
* Added mockserver build type for switching to mocked network data
* Grabbed JSON for the various URLs by hitting the TMDB site manually and had to modify page 1 of results to only be 1 page of results
* Added MainActivityTest for instrumentation tests on the main activity
* Modified MoviesViewModelTest, MoviesPagingSourceTest, and MovieMapperTest to use JSON files instead of creating the data models directly; puzzled about request to unit test the MoviesGrid with a mock webserver because it's a PagingSource underneath and the Android docs say to use a mock API which is what's done already (https://developer.android.com/topic/libraries/architecture/paging/test)
* Slight cleanup of code layout
* Added Kover and was surprised unit tests only have 15% line coverage (found out Kover doesn't support instrumentation tests)
* Pulled most of the testing utils into the :lib:testing:unittest and :lib:testing:androidtest modules
* Added a mockwebserver test rule to reduce boilerplate and provide an API for unit/instrumentation tests

## Future Improvements
* Reorganize codebase so it's better modularized/layered
* Add Paparazzi testing for screenshot unit tests
* Add Jacoco for full coverage reports
* Resurrect okreplay to make capturing/updating network data easier for mock web server tests

## Other Notes
* I think I hit a bug in Hilt and wasn't able to get @TestInstallIn to work to replace the base URL so ended up using a build variant