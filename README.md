# zipkin-mongodb
Shared libraries that provide Zipkin integration with MongoDB. Requires JRE 8.

*This is not production ready at the moment. Things will change!*

## Quick Start
Make sure you have [MongoDB](https://www.mongodb.com/download-center/community) 4.x and it is running.

Then, package and start Zipkin with MongoDB support.

```bash
$ curl -sSL https://zipkin.io/quickstart.sh | bash -s
$ curl -sSL https://github.com/adriancole/zipkin-mongodb/releases/download/latest/mongodb.jar > mongodb.jar
$ STORAGE_TYPE=mongodb \
    java \
    -Dloader.path='mongodb.jar,mongodb.jar!/lib' \
    -Dspring.profiles.active=mongodb \
    -cp zipkin.jar \
    org.springframework.boot.loader.PropertiesLauncher --zipkin.ui.source-root=classpath:zipkin-lens
```

After executing these steps, applications can send spans to
http://localhost:9411/api/v2/spans (or the legacy endpoint http://localhost:9411/api/v1/spans)

If you have any traces, they will show up at http://localhost:9411/zipkin

## Storage
The library that persists and queries collected spans is called
`StorageComponent`. The [storage](storage) module supports the Zipkin Api and all
collector components.

## Autoconfigure
The component in a zipkin server that configures settings for storage is
is called auto-configuration, a Spring Boot concept. The [autoconfigure](storage)
module plugs into an existing Zipkin server adding MongoDB support.
