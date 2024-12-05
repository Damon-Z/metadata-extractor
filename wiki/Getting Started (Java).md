`metadata-extractor` is available via [Maven](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.drewnoakes%22%20AND%20a%3A%22metadata-extractor%22), or via a download from the [releases page](https://github.com/drewnoakes/metadata-extractor/releases).

Ensure both JAR files are available on your classpath: `metadata-extractor-x.x.x.jar` and `xmpcore-x.x.x.jar` (where `x.x.x` represents the version you're using).

Using this library is straightforward.  Conceptually there are two steps to its usage:

1. Read a `Metadata` object from an image
2. Query the `Metadata` object to retrieve one or more values

NOTE:

* These examples are based upon version 2.7.2 of the API.  Earlier versions may differ slightly.
* `import` statements and exception handling code have been omitted for clarity.
* Fully-featured sample classes are [available in the source distribution](https://github.com/drewnoakes/metadata-extractor/tree/master/Samples/com/drew/metadata).

----

## 1. Read `Metadata`

The simplest way to read metadata is to use `ImageMetadataReader`.

If you know the type of file, you can replace `ImageMetadataReader` with the specific reader, for example `JpegMetadataReader`. Using `ImageMetadataReader` is often safer as it uses `FileTypeDetector` to inspect the file's contents in order to determine which decoding method to use and incurs very little overhead.

### From a File

For an image file of any supported type, use:

```java
File jpegFile = new File("myImage.jpg");
Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
```

### From a stream

Reading from a stream is much the same:

```java
Metadata metadata = ImageMetadataReader.readMetadata(stream);
```

-----

## 2. Query `Tag`s

A `Metadata` object contains zero or more `Directory` objects.  These in turn contain zero or
more `Tag` objects.  Tags hold values representing the metadata for the source image.

### Print out all `Tag` values

```java
for (Directory directory : metadata.getDirectories()) {
    for (Tag tag : directory.getTags()) {
        System.out.println(tag);
    }
}
```

### Query the raw value of a specific `Tag`

```java
// obtain the Exif directory
ExifSubIFDDirectory directory
    = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

// query the tag's value
Date date
    = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
```

Note that tag values have specific data types.  Attempts will be made to convert to the type you
request, but this is not always possible.

### Query a decoded description of a specific `Tag`

If you only wish to display a friendly string version of the tag's value, simply call `Tag.toString()`.
Alternatively, you can wrap the directory with a `Descriptor` and have strongly-typed access with
descriptive method names:

```java
// obtain a specific directory
Directory directory
    = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

// create a descriptor
ExifSubIFDDescriptor descriptor
    = new ExifSubIFDDescriptor(directory);

// get tag description
String program = descriptor.getExposureProgramDescription();
```

In the above example, `program` would contain a detailed string such as `"Manual control"` or
`"Aperture Priority"`, whereas the raw tag value is actually an integer.  Descriptors not only
decode enum values, but also prepend/append units (`"-1 EV"`, `"f/2.8"`) and provide methods that
calculate derived properties for convenience.