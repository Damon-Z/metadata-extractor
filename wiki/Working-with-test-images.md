# Test media

The metadata-extractor project maintains a set of media files (images, video, audio) for experimentation and testing. Most of these images were kindly donated by users of the library, and we welcome contributions that extend the set in interesting ways. Please ensure you have permission to share any images you donate.

Regression tests produce output from both the Java and .NET implementations, allowing each to track changes over time. It also encourages parity between the two implementations, allowing code to be shared between each with some minor modifications.

This test data lives in its own git repository ([source](https://github.com/drewnoakes/metadata-extractor-images), [wiki](https://github.com/drewnoakes/metadata-extractor-images/wiki)). It's quite a large repository, requiring some time and space to clone. Thankfully you should only have to do that once.

# Running regression tests

When working on the metadata-extractor library it's important to test for regressions using the test data.

To do this, you should check out all three repositories side-by-side:

```bash
$ git clone git@github.com:drewnoakes/metadata-extractor.git
$ git clone git@github.com:drewnoakes/metadata-extractor-dotnet.git
$ git clone git@github.com:drewnoakes/metadata-extractor-images.git
```

The Java library must be built first.

Then, open the .NET solution in the `metadata-extractor-images/src/dotnet` folder. This contains a single project that will run both the Java and .NET libraries on every file from the `metadata-extractor-images` library, writing the resulting output to text files, then producing diff files between the Java and .NET versions. These text files are committed to the repository, meaning you can use standard `git` tooling to highlight any changes to the output.

You can also browse the text files to look for places where the libraries differ, or for errors, and use that information to improve the library.

# Unit testing

Unlike most projects, we tend to avoid adding many unit tests. Instead, we use the `metadata-extractor-images` repo to track regressions. Often, adding several variations of images that exercise a piece of code does a better job of ensuring correctness than a handwritten unit test.

# Extracting segments from test images

However, there are cases that require bytes from the image file, usually from a known container (e.g. JPEG segment). Instead of adding the whole image, you can add a (small) segment to the source repository to assist with testing. There are several unit tests that demonstrate this process.

* Java [`ExifReaderTest.testStackOverflowOnRevisitationOfSameDirectory`](https://github.com/drewnoakes/metadata-extractor/blob/main/Tests/com/drew/metadata/exif/ExifReaderTest.java#L172)
* .NET [`ExifReaderTest.TestStackOverflowOnRevisitationOfSameDirectory`](https://github.com/drewnoakes/metadata-extractor-dotnet/blob/main/MetadataExtractor.Tests/Formats/Exif/ExifReaderTest.cs#L146)

The library has a command line tool for extracting segments from JPEG files.

* Java [`com.drew.tools.ExtractJpegSegmentTool`](https://github.com/drewnoakes/metadata-extractor/blob/main/Source/com/drew/tools/ExtractJpegSegmentTool.java)
* .NET [`MetadataExtractor.Tools.JpegSegmentExtractor.Program`](https://github.com/drewnoakes/metadata-extractor-dotnet/blob/main/MetadataExtractor.Tools.JpegSegmentExtractor/Program.cs)

But again, before extracting segments, ask yourself whether adding the image to the _metadata-extractor-images_ repository, manually checking the initial metadata file, and later seeing regressions (or improvements) happen automatically via diffs is very useful. Testing the whole image does a much better job of assuring the software works correctly in most cases.
