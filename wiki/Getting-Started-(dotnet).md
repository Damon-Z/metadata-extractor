`metadata-extractor` is available via [NuGet](https://www.nuget.org/packages/MetadataExtractor/),
or via a download from the [releases page](https://github.com/drewnoakes/metadata-extractor-dotnet/releases).

Using this library is straightforward.  Conceptually there are two steps to its usage:

1. Read `Directory` objects from an image
2. Query the `Directory` objects to retrieve one or more values

NOTE:

* These examples are based upon version 2 of the API.  Earlier versions may differ slightly.
* `using` directives and exception handling code have been omitted for clarity.
* Fully-featured sample classes are [available in the source distribution](https://github.com/drewnoakes/metadata-extractor-dotnet/tree/master/MetadataExtractor.Samples/).

----

## 1. Read Metadata

The simplest way to read metadata is to use `ImageMetadataReader`.

If you know the type of file you're reading from then you can replace `ImageMetadataReader` with
a more specific reader, such as `JpegMetadataReader`. Using `ImageMetadataReader` is often safer
as it uses `FileTypeDetector` to inspect the file's contents in order to determine which decoding
method to use and incurs very little overhead.

### From a File

For an image file of any supported type, use:

```c#
var directories = ImageMetadataReader.ReadMetadata("myImage.jpg");
```

### From a stream

Reading from a `Stream` is much the same:

```c#
var directories = ImageMetadataReader.ReadMetadata(stream);
```

-----

## 2. Query `Tag`s

After reading metadata, you'll have zero or more `Directory` objects.  These in turn contain zero or
more `Tag` objects.  Tags hold values representing the metadata of the source image.

### Print out all `Tag` values

```c#
foreach (var directory in directories)
{
    foreach (var tag in directory.Tags)
    {
        Console.WriteLine(tag);
    }
}
```

### Query the raw value of a specific `Tag`

```c#
// obtain the Exif SubIFD directory
var directory = directories.OfType<ExifSubIfdDirectory>().FirstOrDefault();

if (directory != null)
{
    // query the tag's value
    if (directory.TryGetDateTime(ExifDirectoryBase.TagDateTimeOriginal, out var dateTime))
        return dateTime;
}
```

Note that tag values have specific data types.  Attempts will be made to convert to the type you
request, but this is not always possible.

### Query a decoded description of a specific `Tag`

If you only wish to display a friendly string version of the tag's value, simply call `Tag.ToString()`.
Alternatively, you can wrap the directory with a `Descriptor` and have strongly-typed access with
descriptive method names:

```c#
// obtain a specific directory
var directory = directories.OfType<ExifSubIfdDirectory>().FirstOrDefault();

if (directory != null)
{
    // create a descriptor
    var descriptor = new ExifSubIfdDescriptor(directory);
    
    // get tag description
    String program = descriptor.GetExposureProgramDescription();
}
```

In the above example, `program` would contain a detailed string such as `"Manual control"` or
`"Aperture Priority"`, whereas the raw tag value is actually an integer.  Descriptors not only
decode enum values, but also prepend/append units (`"-1 EV"`, `"f/2.8"`) and provide methods that
calculate derived properties for convenience.