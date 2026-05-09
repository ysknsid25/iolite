# IDE

IntelliJ

# setup

```bash
chmod +x .githooks/pre-commit
```

# Contributing

**Contributions that do not follow these procedures may be ignored or closed**.

## feature request or light fix or add new docs?

When developing a new feature or modifying an existing one, please file an issue first before creating a PR. Label the issue with `enhancement`.

If you want to fix a bug or security issue, please first file an issue with the `bug` label.

Then fill out the form according to the contents of the issue template.

## Pull Request

Please write your Pull Request according to the contents of the Pull Request template.

As a general rule, the title of your Pull Request should be `#{issue number} - {summary}`.

And make sure that your CI has passed.

## steps

1. Fork the repository
2. Create a new branch (`git checkout -b feature/{issue no}`)
3. Make your changes
4. Commit your changes (`git commit -m "fix: {issue no} - {summary}"`). 
   - Use `fix:`, `feat:`, `docs:`, `style:`, `refactor:`, `perf:`, `test:` as the commit type.
   - Use the issue number in the commit message.
5. Push to the branch (`git push origin feature/{issue no}`)
6. Create a new Pull Request

## Binary compatibility

iolite is a published library, so the JVM `.jar` is consumed by downstream projects without
recompilation. To prevent accidental breakage we use the
[Binary Compatibility Validator](https://github.com/Kotlin/binary-compatibility-validator)
plugin and Kotlin's `explicitApi()` mode.

The current public API is committed under `api/iolite.api`. CI runs `./gradlew apiCheck`
on every PR — if your change touches the public API surface, the job will fail until the
dump is updated.

### When you change the public API

1. Run `./gradlew apiDump -PdisableKlibApi=true` (omit the property in environments that
   support every Native target).
2. Inspect the diff in `api/iolite.api`. **Removed or changed signatures are binary-breaking.**
3. Commit the updated `.api` file as part of your PR.

### Reviewing the diff

- **Adding** a new declaration is safe and only requires the dump update.
- **Removing** a declaration, **changing a return type / parameter list**, or **tightening
  visibility** breaks downstream consumers. Either keep the old signature alive — usually
  via an overload or `@Deprecated(level = DeprecationLevel.HIDDEN)` shim — or call out the
  break in the PR and bump the major version.
- Adding a parameter with a default value to an existing function is **not** binary
  compatible on the JVM. Add a new overload instead.

### Explicit API mode

Public declarations must spell out their visibility (`public`) and return type. The
compiler enforces this. When something should be internal, mark it `internal` or `private`
explicitly.
