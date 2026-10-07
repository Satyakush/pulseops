# Local Development Workflow

A productive local workflow keeps configuration deterministic.

1. Use the local Spring profile for development-specific settings.
2. Keep credentials in the configured local/test mechanism rather than source-controlled production secrets.
3. Start the application with Maven.
4. Verify the health endpoint before exercising protected APIs.
5. Use a stable request id while debugging a multi-step request flow.
6. Run the focused test suite before committing changes.

The local profile exists to make development repeatable without changing production behavior.
