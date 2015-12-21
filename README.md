# orgz

##build
Open the project in IntelliJ IDEA 15 (Community Edition is fine) and choose `Build | Build Artifacts...` and then select `orgz:jar` from the menu.

##run
`java Orgz org_filename users_filename [report_filename]`

##tests
Tests are implemented using `junit`. In the IDE, open `OrgFileLoaderTest` and in the margin next to the class declaration, click on the test runner icon.

##adding tests
There are two unit tests (one negative and one positive) that just make a call to one of the `verifyxxx` methods.

To add additional tests,
create input files in the project, and then create a unit test that calls the appropriate `verifyxxx` method with the file resource names.

##notes
* The specified internal API was challenging to use to implement tool: the `OrgBean` did not report its ID => was difficult to complete the report portion of the load; I chose to extend the contract.
I also chose to view `OrgBean` and `OrgCollection` as interfaces, such that alternate implementations could to choose to virtualize the data
for large data sets.
* Some of the data in the files is not used (org name and user ID), so it was ignored.
* The load uses a tortured path to immutability, chosen to minimize the number of object creations and ensure that only one representation of the tree was in memory at a time
(to maximize the amount of data that could be loaded). The interfaces present an immutable view of the tree, but the implementation is actually mutable
until `freeze` is called on the tree. This provides a good consumer experience at the expense of the maintainers'/readers' sanity.
* This code should be able to handle up to 2 billion lines in the users data file (it is simply aggregating the data from that stream).
* The number of orgs in the orgs file will be limited by main memory. The loader and representation are pretty lean => should be able to load quite a few.

