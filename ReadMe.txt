-----------------------------------HttpRequestTest-----------------------------------------------Android Make Http request & JSON data parsing
error
HttpClient is not supported any more in sdk 23
solution:
add following in gradle file

useLibrary 'org.apache.http.legacy'
in
android {
}

http://hmkcode.com/android-parsing-json-data/


fake rest api
https://fakerestapi.azurewebsites.net/swagger/ui/index#!/Users/Users_Post

https://jsonplaceholder.typicode.com/




------------------------------------------------------------------------------------------------
JSON parsing

for single Obj
https://examples.javacodegeeks.com/core-java/json/java-json-parser-example/
https://www.tutorialspoint.com/android/android_json_parser.htm