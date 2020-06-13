# Kotlin JSON Serialization Comparison

A quick speed test on various Kotlin JSON serialization libraries.

Compares

- [Gson](https://github.com/google/gson)
- [Moshi](https://github.com/square/moshi)
- [Kotlin serialization](https://github.com/Kotlin/kotlinx.serialization)

## Building and Running

### Requirments

- Java
- Kotlin

### Command

```bash
./gradlew run
```

## Results

```
-------------------------------------------
Re-use Gson for various classes
{"id":1}
{"id":1,"str":"string1"}
{"id":2}
B(id=3, str=null)
Parsing will go through but trying to access it will throw an java.lang.NullPointerException
-------------------------------------------
Try Moshi
{"id":1}
Parsing "{ "id": 3, "str": null }" errors with com.squareup.moshi.JsonDataException: Non-null value 'str' was null at $.str
-------------------------------------------
Kotlin Serializer
{"id":1}
Parsing "{ "id": 3, "str": null }" errors with kotlinx.serialization.json.JsonDecodingException: Unexpected JSON token at offset 18: Expected string literal with quotes. Use 'JsonConfiguration.isLenient = true' to accept non-compliant JSON.
 JSON input: { "id": 3, "str": null }
-------------------------------------------
Running serialization 10000 times.
Gson: 40ms
Moshi: 49ms
Kotlin Serialization: 27ms
-------------------------------------------
Running deserialization 10000 times.
Gson: 85ms
Moshi: 88ms
Kotlin Serialization: 27ms
-------------------------------------------
```
