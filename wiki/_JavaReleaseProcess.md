# Java Release Process

## Useful links

- [Sonatype OSS & Maven][1]

## Build PC set up

READ THIS: http://central.sonatype.org/pages/apache-maven.html

### Maven

Make sure to use Maven 3. Can use Maven from IntelliJ.

```
alias mvn=/c/Users/drnoakes/AppData/Local/JetBrains/Toolbox/apps/IDEA-C/ch-0/232.10227.8/plugins/maven/lib/maven3/bin/mvn
```

### Java

Set the `JAVA_HOME` variable if necessary (must be at least 1.8):

```
export JAVA_HOME="C:\Program Files\Java\jdk-18.0.2.1"
```

### GPG

Need to install GPG.

http://blog.sonatype.com/2010/01/how-to-generate-pgp-signatures-with-maven/

```
gpg --gen-key
gpg --list-keys
gpg --list-secret-keys
gpg --keyserver keyserver.ubuntu.com --send-keys <PUT_KEY_HERE>
```

## Quality checks

- Run all unit tests.

## Versioning

- Set the `<version>` element correctly in `pom.xml`. Note that `-SNAPSHOT` deployments
  are not released to Maven Central.

- Update the `<version>` element in `README.md`'s POM fragment

- Commit the version number changes.

## Stage

- Clean, build, then deploy to Sonatype OSS staging for review:

```
mvn clean deploy
```

- Review the deployed artifacts via the [Sonatype OSS web UI][2].

## Release

Once happy with the deploy, it can be released via:

```
mvn nexus-staging:release
```

Or to drop it:

```
mvn nexus-staging:drop
```

After a time, it will appear on [Maven Central][3].

## Tagging

- Tag the commit:

```
git tag 2.17.0
```

## Push

```
git push
git push --tags
```

## Make GitHub release

https://github.com/drewnoakes/metadata-extractor/releases

- write release notes on [releases][4] page
- copy release notes to wiki's [ChangeLog][5]

## Prepare the next release

- Bump the version number in `pom.xml` (as a `-SNAPSHOT`) and `build.xml`.
- Commit this as `Starting version x.x.x`.

[1]: http://central.sonatype.org/pages/apache-maven.html
[2]: https://oss.sonatype.org/#nexus-search;quick~drewnoakes
[3]: http://search.maven.org/#search%7Cga%7C1%7Cdrewnoakes
[4]: https://github.com/drewnoakes/metadata-extractor/releases
[5]: https://github.com/drewnoakes/metadata-extractor/wiki/ChangeLog
