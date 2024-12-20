**NOTE:** [See the releases page for a version which includes hyperlinks to issues and downloads.](https://github.com/drewnoakes/metadata-extractor/releases)

# 2.9.0

_Released 5 Apr 2016_

This release contains fixes across the entire project uncovered during the creation of the [official .NET port of this project](https://github.com/drewnoakes/metadata-extractor-dotnet). This new implementation was originally ported from Java to C# by Yakov Danilov (for Imazen LLC). Since then it's been heavily refactored. Going forward, it will be kept functionally equivalent to the Java implementation.

Full diff: https://github.com/drewnoakes/metadata-extractor/compare/2.8.1...2.9.0

New features

- Support for RAF files #73 #109
- Add support for JFXX (JFIF extension) segments
- Add support for "Ducky" segments, from Photoshop's "Save for Web" feature #115
- Read JFIF thumb width/height tags
- Process XMP found in Exif tag 0x02BC #157
- Process sub-IFD found via Exif tag
- Support non-standard TIFF format code 13 that points to new IFDs
- Support arrays of IFD pointers within a tag, not just a single pointer
- Process additional XMP tags #159
- Support additional WebP chunk types

Fixes

- Considerable overhaul of date, time and time zone handling across the library #153 #154 #155 #158
- Attempt to recover from incorrect JPEG segment length by scanning for next marker #121 #129
- Incorrect PNG chromaticity
- Potential bound exception in `PhotoshopDirectory`
- Potential bound exception in `NikonType2MakernoteDescriptor`
- Incorrect return value from `AdobeJpegDescriptor.getDctEncodeVersionDescription`
- Typos in `PcxDirectory` tag names
- Validate Olympus makernote dates
- Validate ICC dates so exception won't halt reading
- Validate IPTC dates during parsing
- Parse IPTC `IptcDirectory.TAG_DIGITAL_DATE_CREATED`
- Non-`8BIM` Photoshop IRBs don't stop processing #125 #128
- `TiffReader` attempts to recover from invalid byte ordering #136
- Correctly handle alternative Olympus makernote preamble #93
- Clear out fractional seconds from computed dates (PNG and ICC) #146
- Fix swapped `JpegComponent` methods `getHorizontalSamplingFactor()` and `getVerticalSamplingFactor()` #156
- Make `GpsDirectory.getGpsDate()` locale independent #160 #161
- Make `PngMetadataReaderTest` locale independent #162 #163

Description formats

- Standardisation of f-stop descriptions
- Change formatting of focal length descriptions
- Use offset in date descriptions, not timezone name
- ICC value formats
- Fix typo in 'fluorescent' in `ExifDescriptorBase`
- Remove trailing space in `PhotoshopDescriptor` output
- Remove trailing full stop in `ExifDescriptorBase` output
- Number formatting in Olympus makernote descriptions
- Don't show lens f-stop range when upper and lower values are identical
- Update shutter speed value formatting
- Describe `ExifDescriptorBase.TAG_LENS_SPECIFICATION`
- Capitalise the name of `XmpDirectory`

API changes

- Deprecate `ExifThumbnailDirectory.TAG_THUMBNAIL_COMPRESSION`
- Remove unused class `DefaultTagDescriptor`
- Fix typos in some `OlympusMakernoteDescriptor` description methods
- In `GifHeaderDirectory` replace `TAG_TRANSPARENT_COLOR_INDEX` with `TAG_BACKGROUND_COLOR_INDEX` #142
- In `JfifDirectory` replace `getImageWidth`/`Height` with `getResX`/`Y` #143
- Deprecate `ExifDirectoryBase`'s `TAG_LIGHT_SOURCE` in favour of `TAG_WHITE_BALANCE`

Performance

- Considerable improvement when reading large TIFF files (such as raw files) using `ImageMetadataReader` #164
- Perform type check outside loop
- Use Collections.singletonList instead of Arrays.asList with single item
- Slightly reduce allocations in PNG reader
- Simplify `Tag.getTagTypeHex()`

Miscellaneous changes

- `Metadata` stores and returns `Directory` instances in the order they were created
- `FileTypeDetector` throws if stream doesn't support mark/reset
- unexpected PNG ICC compression method is registered as an error
- Project now has an SVG logo which looks better on high DPI displays
- Bump junit version
- Make Javadoc compilable under Java 8 #137
- Many documentation and internal fixes

# 2.8.1

_Released 20 Apr 2015_

* When a parsing date with no timezone specified, don't use default (assume UTC/GMT) (#97)
* Formatting of f/stop descriptions changed to `f/2.8` instead of `F2.8`(#97)
* Extend Exif exposure program decoding (#97)
* Add Canon LensType tag (#97)
* Handle XMP data with different (erroneous yet common) preamble in JPEG files (#102)

# 2.8.0

_Released 22 Mar 2015_

* WebP support (#85)
* File system metadata (#86)
* ICO file support (#61)
* PCX support (#89)
* Improve Exif tag comprehension across directory types (#3, #8, #9, #82, #83)
* Support PNG `sBIT` and `pHYs` chunks
* Extract `tEXt` and `iTXt` chunks from PNG (#62)
* `Metadata` may now hold multiple instances of a `Directory` subclass (#61, #65)
* Improve date/time formatting of EXIF-GPS and IPTC data
* Detect new file types: RIFF (#85), PCX (#89), ICO (#61), additional ORF format
* Read image resources from Photoshop data (#87)
* Fix decoding of 8BIM Photoshop strings (#57)
* Process EXIF, ICC and XMP data embedded in Photoshop data (#88)
* Extend XMP support (#92)
* Extend Olympus makernote support (#93)

# 2.7.2

_Released 30 Jan 2015_

* Bug Fixes
  * Fix `Tag.hasTagName()` to return a boolean

# 2.7.1

_Released 26 Jan 2015_

* Fixes
  * Fix exception related to IPTC character encoding
  * Fix exception caused by zero-length IPTC tags
  * Mute exceptions about PNG chunks we don't even process
* Improvements
  * Decode more IPTC version tags correctly
  * Add `Tag.hasTagName()` and `Directory.hasTagName(int)` methods
  * Extract file type detection to reusable `FileTypeDetector` class
  * Make file type detection more rigorous
  * Detect camera RAW formats (still treated as Exif TIFF)
* API changes
  * Rename `PngDirectory.TAG_PROFILE_NAME` to `PngDirectory.TAG_ICC_PROFILE_NAME`
  * Removed `ExifReader`'s TIFF processing functions. Use `TiffReader` instead.
* Project
  * Regression test [data set](https://github.com/drewnoakes/metadata-extractor-images) increased significantly

# 2.7.0

_Released 7 December 2014_

* Features
  * Added support for PNG, GIF and BMP files.
  * Added makernote support for:
    * Leica
    * Sanyo
    * Ricoh
  * Support for character encoding in IPTC data.
  * Extract IPTC data found in Exif chunks.
* Improvements
  * Optimised JPEG processing, avoiding unnecessary IO.
  * Improved makernote support for:
    * Canon
    * Fujifilm
    * Kodak
    * Panasonic
    * Olympus
    * Sigma
    * Sony
  * Javadoc improvements.
  * Many more unit tests.
* Bug Fixes
  * Fixed incorrect rejection of rare but correct JPEG files.
  * Unit tests work across cultures.
  * Fixed bug in Lat/Lng coordinates containing a zero value.
* API Changes
  * Some `Casio*.TAG_CASIO_*` renamed to `Casio*.TAG_*`
  * Some `Olympus*.TAG_OLYMPUS_*` renamed to `Olympus*.TAG_*`
  * Some `Gps*.TAG_GPS_*` renamed to `Gps*.TAG_*`
* Project
  * Migrated project to GitHub.
  * Added Maven `pom.xml` project file.
  * Targeting JDK 1.5 instead of 1.6.
  * Update XMPCore to version 5.1.2.
  * Update junit to version 4.11.0
  * Introduce [Travis-CI build](https://travis-ci.org/drewnoakes/metadata-extractor)

There are many changes in this release. Please [open an issue](https://github.com/drewnoakes/metadata-extractor/issues)
if you encounter an API change which is not listed here.

# 2.6.4

_Released 23 December 2012_

 * Targeting JDK 1.6 instead of 1.7.
 * Added XMP Rating tag.

# 2.6.3

_Released 28 October 2012_

 * Added support for Olympus ORF & Panasonic RW2 RAW files which use non-standard TIFF markers.
 * Better default descriptions of long array tag values.
 * Fixed some minor documentation issues.
 * Cause `ImageMetadataReader.java` to truncate tag descriptions that are longer than 1024 characters.
 * Extended reporting from `ProcessAllImagesInFolderUtility.java`.
 * Updated templating for online Javadoc, as generated by Java 7.

# 2.6.2

_Released 28 May 2012_

 * Fixed a bug in `RandomAccessFileReader` which would result in string values failing to read correctly from files.  Thanks to Bruce Grant for reporting this issue.

# 2.6.1

_Released 24 May 2012_

 * Fixed a bug in `CanonMakernoteDescriptor.getFlashDetailsDescription()` where the description was shown as a meaningless integer instead of a string such as "Internal flash" or "External E-TTL"

# 2.6.0

_Released 22 May 2012_

Highlights:

 * Much faster processing of large TIFF files
 * Easier use of GPS data
 * Basic support for PSD files

Detail:

 * Added `GeoLocation` type and updated the means of extracting GPS data from the `GpsDirectory`
 * Added a new sample application which produces Google Maps HTML for a directory of photos which contain GPS data
 * Converted `BufferReader` from a class to an interface, renaming the prior implementation as `ByteArrayReader`
 * Created a new `RandomAccessFileReader` which allows the processing of files without loading the whole file into memory
 * Added `PsdMetadataReader`, `PsdReader`, `PsdHeaderDirectory` and `PsdHeaderDescriptor` classes which offer some basic support for PSD files
 * Changed base class's `MetadataReader.extract` method to take a `BufferReader` instead of a `byte[]`

# 2.5.1

_Released 20 May 2012_

 * Proper handling of all signed/unsigned integer types supported by Exif and Tiff data in general
 * Fixed bug in processing of unsigned rational values
 * Converted project hosting from SVN to Git
 * Rearranged project structure slightly
   * Tests now have their own folder
   * Sample code now has its own folder

# 2.5.0-RC3

_Released 18 Nov 2011_

 * Fixed a bug where adding an offset and length resulting in integer overflow was incorrectly passing a bounds check
 * Added unit test for `IccReader`
 * Added exception handling for parsing of numeric types from strings in `XmpReader`
 * Fixed bug in parsing of JFIF data
 * Added JFIF parsing unit test
 * Removed ISO description check that multiplied ISO values less than 50 by 200
 * Created utility class `ExtractAppSegmentBytesToFileUtility` for writing specific JPG segments to files, and reading them again into `byte[]` for convenience in unit testing (and keeping source code file size down)

# 2.5.0-RC2

_Released 27 May 2011_

 * Created new `Age` class for more meaningful representation of recognised people's ages in Panasonic camera photos.
 * Created descriptor methods for additional Panasonic makernote tags (for baby ages and text stamps)
 * Fixed a bug where `PanasonicMakernoteDescriptor.getTitleDescription` was returning the text associated with `TAG_LANDMARK`
 * Created `PanasonicMakernoteDirectory.getAge` method for raw access to the `Age` object associated with certain tags
 * Added missing `hashCode()` method to `Face` class
 * Added missing getters to `Face` class
 * Fixed logic that checks for the end of a printable string in `PanasonicMakernoteDescriptor`
 * Fixed annotation issue
 * Made the `XMPMeta` object instance available on `XmpDirectory` for general consumption
 * Added support for `AdobeJpegDirectory` and related classes, which comes from the APPE JPEG segment, when present
 * Additional API documentation
 * Dealt with some incorrect code analysis warnings

# 2.5.0-RC1

_Released 9 May 2011_

 * New metadata support
   * Added support for XMP metadata
   * Added support for Photoshop metadata embedded in Jpeg files
   * Added support for ICC colour profiles
   * Added support for JFIF metadata
   * Added many more tags to IPTC directory

 * Makernotes
   * Improved support for Panasonic makernotes, including decoding of detected and recognised faces in images
   * Wider support for Canon makernote data
   * Renamed existing `SonyMakernote` classes to `SonyType1Makernote`
   * Added tags to the previously empty `SonyType1MakernoteDirectory`
   * Added support for Sony Type 6 makernotes
   * Added support for Sigma/Foveon camera makernotes
   * Added support for many more Panasonic makernote tags
   * Considerable additions to Nikon Type 2 makernote handling (covering modern Nikon cameras)

 * Reliability
   * Converted all access to `byte[]` in `ExifReader` to be via `BufferReader`, ensuring proper bounds checking during processing
   * `JpegMetadataReader` no longer assumes that certain segments always hold one type of metadata, cycling through the segments available and opportunistically detecting segments that can be processed
   * Now stopping IFD processing when an invalid format type is encountered, helping to avoid populating directories with lots of rubbish from corrupt files
   * Can now decode version numbers of form `[0 1 0 0]` as well as `[48 49 48 48]`
   * Introduced new checked `BufferBoundsException` which is thrown by all `BufferReader` methods, ensuring that calling code properly handles cases when we run out of data
   * Applied `@Nullable` and `@NotNull` attributes across entire codebase

 * Unit Tests
   * Significantly improved IPTC unit testing.
   * Added unit tests for Sony Type 1 and Type 6 makernotes
   * Updated all unit tests to jUnit 4
   * `AllTests` class no longer needed and removed
   * Effort to make unit tests work in non-English locales

 * Bugs
   * Fixed a potential bug in `ExifReader` around handling of float32/64 values.  This may never have been seen as it doesn't seem that these types are ever used in Exif data
   * Fixed a potential exception in `Directory.getString(int)` when the underlying value is an array of primitives other than int
   * Fixed typo in `GpsDirectory` which incorrectly confused the `TAG_GPS_IMG_DIRECTION_REF` and `TAG_GPS_IMG_DIRECTION` tag names
   * Corrected integer value assigned to tag `TAG_GPS_AREA_INFORMATION`
   * Added proper decoding of `GpsDirectory.TAG_GPS_VERSION_ID`
   * Introduced Yuri's fix for the various `SOF0-SOFF` Jpeg segments
   * Improved Exif directory 'user comment' decoding for various character sets.
   * Avoid `OutOfMemoryError` if the advertised size of the image thumbnail is larger than the available bytes in the Exif segment

 * Public API changes
   * Metadata and Directory are now both iterable, so they support `foreach` now
   * Removed checked `MetadataException` from all description getters
   * Refactored all `Reader` classes to take their data in the extract method, rather than in their constructors, offering thread safety and allowing reuse
   * Created method `Metadata.hasErrors()` that reports whether there exists an error in any directory
   * Separated Exif IFD1 (thumbnail) tags out from the IFD0 tags and into the new `ExifThumbnailDirectory` with `ExifThumbnailDescriptor`
   * Split `ExifDescriptor` into `ExifIFD0Descriptor` and `ExifSubIFDDescriptor` to mirror actual IFD structure in physical metadata, preventing against tag overwrites due to merging, and making later support for writing metadata easier
   * Added an overload of `Directory.getString` that accepts a charset
   * Use of generics
     * Replaced several instances of `ArrayList` and `HashMap` with their generic counterparts
     * Genericised methods on Metadata for obtaining Directory objects, removing several casts
     * Made `TagDescriptor` generic
     * Added generic constraint upon `Metadata.getDirectory` and `Metadata.containsDirectory` methods and removed cast and check that are subsequently unnecessary
     * Made descriptor constructors take more specific directory types
   * Added overload of `Directory.getDate` that accepts a `TimeZone` to be used if the underlying value is a string and must be parsed
   * Renamed `ExifDirectory.TAG_COMPRESSION` to `ExifDirectory.TAG_THUMBNAIL_COMPRESSION`
   * Removed some incomplete and unused constants on `ExifDirectory`
   * Removed dependency upon `com.sun.image.codec.jpeg` in `JpegMetadataReader`
   * Created hashCode method for `Rational` as it overrides equals
   * Removed old deprecated constructors for reader classes

 * Internal API changes
   * `Rational` now backed by long, not int
   * Considerable simplification of interface `MetadataReader` and all its implementations
   * Overhauled means of reading different types from `byte[]}} across all `Reader` classes via use of new `BufferReader` class
   * Created methods to add arrays of float/double to a `Directory`
   * Renamed `Metadata.getDirectory` as `getOrCreateDirectory`, and made a new method `getDirectory` that doesn't lazily create a directory
   * Metadata readers only accept non-null `byte[]` as a data source
   * Added overload of `BufferReader.getString` that takes a charset
   * Simplified `ExifReader` by removing some fields so that state is now mostly maintained on the stack
   * `JpegSegmentReader.readSegment()` no longer throws `JpegProcessingException`, simplifying some call sites too

 * Findbugs support
   * Addressed a few more minor Findbugs warnings
   * Disabled Findbugs inspections on `XMPCore` module

 * Serialisation
  * `Directory` and `Descriptor` are no longer serialisable
  * Set `serialVersionUID` on all serialisable exception types, and `Rational`

 * Miscellaneous
   * Added Javadoc to SVN for viewing via Google Code
   * Updated source code headers with Apache 2 License banner
   * Excluded `Output` folder from IntelliJ project
   * `ImageMetadataReader.main` now has an option to output Wiki format tables to more easily show sample output information on the project's wiki
   * `ImageMetadataReader.main` now accept multiple file paths
   * Several spelling mistakes corrected
   * Removed commented code
   * Removed compiler warnings
   * Added some more sample images

# 2.4.0-beta-1

_Released 14 Dec 2006_

  * Added support for TIFF files and several camera RAW formats.
    * Tested on NEF (Nikon), CRW & CR2 (Canon), ARW (Sony)
    * Thanks to Darren Salomons and Peter Wayner for their help with this.
  * New class `ImageMetadataReader` can be used for all supported file types.
  * Improved Javadoc coverage.
  * A few methods marked deprecated in preparation for future releases.

# 2.3.1

_Released 25 Feb 2006_

  * Fixed copy-and-paste errors in `ExifDescriptor`.  Thanks to Ferret Renaud.

# 2.3.0

_Released 12 Jan 2006_

  * New tags from Exif 2.2 specification (A401-A420).
  * Fixed stack overflow exception in `ExifReader` for cyclic directory references.  Thanks to John Sidney-Woollett for reporting this bug (reported for Fuji FinePix A101 and Canon 20D).
  * Fixed rounding error in the shutter speed description which was giving the wrong value most of the time (for example, 1/32 instead of 1/50). Thanks for Gli Blr and Mark Edwards for pointing out this error.
  * Fix thread safety bug in `ExifReader`.
  * Fixed `OutOfMemoryError` seen in certain Canon 20D images. Thanks to Henry Yeung for providing an image to reproduce this error.
  * Support for Windows XP Exif tags (Author, title, comments, etc).
  * Added more documentation, and removed commented/unused code.
  * Enhanced descriptor support for Exif tags.
  * Extract comments in non-ASCII encodings.
  * Improved camera model makernote support:
    * New models:
      * Epson (thanks to David Carlson for pointing me in the right direction with this)
      * Kyocera / Contax (very limited)
      * Minolta (it utilises the Olympus format)
      * Panasonic
      * Pentax / Asahi
    * Improved support for models:
      * Olympus
      * Canon (tested with newer Canon models, including the 20D)
      * Casio (for more modern models)
  * Source distribution file size reduced by using `.metadata` files, rather than entire sample JPEG files. These `.metadata` files contain all non-image JPEG segments, making them suitable for unit tests whilst being much smaller on disk.

# 2.2.2

_Released 22 Nov 2003_

 * Fixed a bug where version strings were assumed to be comprised of exactly four parts, and cases were found where a different number existed

# 2.2.1

_Released 24 Oct 2003_

 * Fixed a bug where `JpegDirectory` had tag names for image width and height around the wrong way.  Thanks to Sander Smith for pointing this out.

# 2.2.0

_Released 18 Oct 2003_

 * Added support for extraction of Jpeg image information (from the SOF0 segment.) Thanks to Darrell Silver for commencing the code for this extension
 * Added support for reading Jpeg comments
 * Additional Nikon camera makernote support for D1/D100 family models. Thanks to Daniel Waeber for providing sample images and to Fabrizio Giudici for publishing his work in decoding this makernote data.
 * Added convenient writing of thumbnails to files from `ExifDirectory`
 * Fixed a bug in date format strings, whereby times in the AM / PM were indistinguishable. Thanks to Bill Boland for being the first person to point this out (this was a popular one!)
 * Fixed bug for multi-component tag values of certain types. Thanks to Derek Wegner for identifying the bug and providing a solution.
 * More unit tests (consequently, the source-code download is much larger.)
 * First version with an Ant build script

# 2.1.0

_Released 12 Jan 2003_

 * Extract methods no longer throw exceptions, with error information stored in `Metadata` instances, using `hasErrors()` and `getErrors()`
 * `Metadata` and dependant classes now serializable for network transmission, and persistance in files & databases
 * Support for extracting metadata from `InputStream`s, such as network connections
 * Replaced code that depended upon JDK 1.4

# 2.0

_Released 10 Dec 2002_

Enormous changes to the class and package structure in this release prohibit a class-by-class breakdown of changes.  The focus is no longer on Exif metadata alone, but now on general metadata extraction from multiple media types.

Changes support:

  * easier future extensibility
  * IPTC metadata extraction
  * multiple directories of tags
  * descriptor class for interpreting values in a given directory
  * multiple media and metadata types
  * enhanced handling of exif makernote values
  * many more unit tests
  * numerous other enhancements
  * minor bug fixes

Simpler extensibility changes the focus from exif extraction alone and opens the scope to general metadata extraction.  Future development will introduce new media and metadata support with little or no impact to existing classes.

# 1.2

_Released 6 Nov 2002_

ExifExtractor.java
  * Proper traversing of Exif file structure and complete refactor & tidy of the codebase (a few unnoticed bugs removed)
  * Reads makernote data for 6 families of camera (5 makes)
  * Tags now stored in directories... use the `IFD_*` constants to refer to the image file directory you require (Exif, Interop, GPS and `Makernote*`) -- this avoids collisions where two tags share the same code
  * Correct extraction of multi-component values
  * No longer decodes image to extract Exif data -- this is much faster
  * Takes componentCount of unknown tags into account
  * Now understands GPS tags (thanks to Colin Briton for his help with this)
  * Returns null when no Exif data present, instead of throwing an exception
  * Some other bug fixes, pointed out by users around the world. Thanks!

ExifLoader.java
  * Removed (unnecessary)

ImageInfo.java
  * Stored IFD directories in separate tag-spaces
  * `iterator()` now returns an Iterator over a list of `TagValue` objects
  * More `get*Description()` methods to detail GPS tags, among others

TagValue.java
  * New class to encapsualte information about a particular tag

Rational.java
  * Improved `toSimpleString()` to factor more complex rational numbers into a simpler form.  I.e. 10/15 -> 2/3
  * `toSimpleString()` now accepts a boolean flag, '`allowDecimals`' which will display the rational number in decimal form if it fits within 5 digits.  I.e. 3/4 -> 0.75 when allowDecimal == true

JpegSegmentReader.java
  * New class to extract APP1 segment (and others) from a Jpeg file -- this avoids decoding images just to get metadata

`tests\*.java`
  * First collection of basic unit tests, to compile against JUnit
  * Doesn't yet cover all classes

Website
  * A collection of JPEGs from various digital camera models, collected on the web and contributed by many users of ExifExtractor
  * Updated documentation

# 1.1.1

Rational.java
  * Added `toSimpleString()` method, which returns a simplified and hopefully more readable version of the Rational.  I.e. 2/10 -> 1/5, 10/2 -> 5

ExifExtractor.java

  * Removed unnecessary casting operations
  * Added a few more comments
  * Removed redundant and commented code (I'm using a CVS system now)

ExifLoader.java
  * Added a much-needed `close()` call to a created input stream, allowing continued use of the File object passed to `ExifLoader.getImageInfo(File)`

ImageInfo.java
  * Make use of new `Rational` method `toSimpleString()` for more elegant output
  * Use of `DecimalFormatter` to tidy output in selected `get***Description()` methods

# 1.1.0

_Released 28 Aug 2002_

  * Descriptive tag values, including units and text for enumerations
  * Decoupling from JDK 1.4-specific libraries (tested with JDK 1.3)
  * More complete list of tags, both as constants for direct lookup, and via the static lookup method

# 1.0

  * Initial release