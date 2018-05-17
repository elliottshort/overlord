## Contributing Code

If you're unsure where to get started, here are some starting points:

  * Issues labeled as
    [Good First Issue](https://github.com/elliottshort/overlord/issues)
    or [Help Wanted](https://github.com/elliottshort/overlord/issues)
  * Checkout the [CodeClimate dashboard](https://codeclimate.com/github/elliottshort/overlord/issues)
    for any technical debt reported and submit a fix
 
### Before you get started

Ready to jump in? Great!

Before you get started on a task:
  * Have you run a quick search of issues and PRs to see if something similar has been suggested or submitted before?
    This doesn't mean you shouldn't attempt this task, but previous discussion could raise issues you'd like
    to address with your work.
  * Multiple PRs with small and targeted improvements are preferable to large changes.
    Smaller PRs are easier to review, so usually get merged quicker.
    If you can break your work down into multiple PRs, then do it.
 
### IDE

Overlord is IDE agnostic.

The project is maven based. Use any IDE that supports maven projects.

### Style

Code style is checked on builds by using [Spotless](https://github.com/diffplug/spotless).
Running `mvn spotless:apply` will format the code to be compliant with this.
It is recommended you run this before creating a Pull Request.

If you wish to have your IDE confirm to the format, check out the plugins for
[google-java-format](https://github.com/google/google-java-format).
 
### Code Quality
[CodeClimate](https://codeclimate.com) is used to monitor code quality.
The dashboard for Overlord is located
[here](https://codeclimate.com/github/elliottshort/overlord).
It does not currently give feedback on PRs, but it can be useful to know the kinds of issues it reports.

## Finally

