taskkill /F /IM chrome.exe /T
cd "D:\GitRepository\ws_bdd"
mvn test -Dcucumber.options="D:\GitRepository\ws_bdd\src\test\Sanity\1Entities\1Verification_of_CreateEntitiesPerson.feature"