# Local Development

Use the local profile when running PulseOps manually:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

The default profile keeps Redis caching disabled, so a local Redis server is not required for the basic application flow. Test credentials are defined only under test resources and are not production secrets.
