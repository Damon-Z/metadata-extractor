XMP is a standard that stores many kinds of metadata in an object model which is usually written to and read from XML.

Although metadata-extractor supports XMP, the current `Directory` class expects all tags to be identified by an integer. XMP uses strings. Therefore, the standard way of iterating tags over a directory doesn't work the same way for `XmpDirectory`.

## Java

You can access the complete `XMPMeta` object via the `XmpDirectory.getXMPMeta()` method.

```java
Collection<XmpDirectory> xmpDirectories = metadata.getDirectoriesOfType(XmpDirectory.class);
for (XmpDirectory xmpDirectory : xmpDirectories) {
    XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
    XMPIterator iterator = xmpMeta.iterator();
    while (iterator.hasNext()) {
        XMPPropertyInfo xmpPropertyInfo = (XMPPropertyInfo)iterator.next();
        System.out.println(xmpPropertyInfo.getPath() + ":" + xmpPropertyInfo.getValue());
    }
}
```

## .NET

You can access the complete `XmpMeta` object via the `XmpDirectory.XmpMeta` property.

```csharp
foreach (var xmpDirectory in directories.OfType<XmpDirectory>())
{
    foreach (var property in xmpDirectory.XmpMeta.Properties)
    {
        Console.WriteLine($"{property.Path}: {property.Value}");
    }
}
```