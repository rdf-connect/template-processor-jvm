# JVM example processor for RDF Connect


## Bump rebuild

Often when a processor is getting developed jitpack doesn't notice so you need to finger it.
Use curl
```bash
# https://jitpack.io/com/github/{user}/{repo}/{branch}-SNAPSHOT
curl -Lis "https://jitpack.io/com/github/rdf-connect/template-processor-jvm/main-SNAPSHOT"
```

After this curl the published index files can be used, like 
```turtle
# https://jitpack.io/com/github/{user}/{repo}/{branch}-SNAPSHOT/{repo}-{bramch}-SNAPSHOT-index.jar
<> owl:imports <https://javadoc.jitpack.io/com/github/rdf-connect/template-processor-jvm/main-SNAPSHOT/template-processor-jvm-main-SNAPSHOT-index.jar>.
```

Note the part in the build.gradle that publishes the `index.ttl` file.

```gradle
// Declare the index.ttl
tasks.register("indexJar", Jar) {
    archiveBaseName.set("index")
    archiveClassifier.set("index")
    from("index.ttl")
}
/* snip */
// Publish it
publishing {
    publications {
        maven(MavenPublication) {
            // fat jar
            artifact(tasks.shadowJar)
            // plain file
            artifact(tasks.indexJar) 
        }
    }
}
```
