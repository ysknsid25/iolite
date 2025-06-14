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
