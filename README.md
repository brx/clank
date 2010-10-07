# Clank #

Clank is a spelling helper written in Scala for lazy typists who write
in languages with funny characters. Using an appropriate
configuration, for example

    "Mogen Sie Gluck?"

becomes

    "Mögen Sie Glück?"

It tries doing this one thing with the least amount of hassle for the
user. Clank doesn't aim to replace traditional spell checkers.

## How it works ##

Based on a user-specified mapping from chars to sets of chars Clank
translates the chars of each input word so that the word is contained
in a user-specified dictionary file. If ambiguities come up Clank
warns the user on *stderr*, listing all possible spellings found.

## Setup ##

### Building from Source ###

To build from source you will need
[sbt](http://code.google.com/p/simple-build-tool/).

To build the standalone JAR, run

    $ sbt update proguard

You will then find it at
*target/scala\_a.b.c/clank\_a.b.c-x.v.z.min.jar*.

### Downloading the standalone JAR ###

There should be standalone JARs of Clank in the download section. The
latest version, I think, is
[here](http://github.com/downloads/brx/clank/clank-0.8.6.jar)!

## Configuration ##

Clank is configured in *$HOME/.clankrc*.

There are 4 options. Each _has_ to be supplied:

- *wordrx*: A regex for matching words in the target language
- *dict*: The target language text dictionary to be used (one word per line)
- *translations*: The character translations in use, each separated by
   :, first character being the src, rest being dst characters
- *maxwordlength*: Maximum length of words to consider

Options are specified one per line in this format:

    optionName = value

Here is an example configuration:

    ~/.clankrc

    wordrx = [a-zA-Z]+
    dict = /usr/share/dict/ngerman
    translations = aä : oö : uü : UÜ : OÖ : AÄ :sß
    maxwordlength = 20

## Usage ##

Clank reads its input text from standard input and outputs either to a
file specified as its first argument or to standard
output. Ambiguities (different word translations that are matched in
the dictionary) are printed on standard error.

### Examples ###

A standalone JAR of Clank is in the current directory and named
clank.jar.

Content of *input_file*:

    Mogen Sie Gluck? Konnen Sie vielleicht helfen? Vielleicht wenn
    Sie einen Keks gehabt hatten, ware es nie dazu gekommen.

The example configuration above is used.

#### Piping from a file and hiding ambiguities while outputting to *stdout* ####

    $ java -jar clank.jar < input_file 2>/dev/null

##### Output (*stdout*) #####

    Mögen Sie Glück? Können Sie vielleicht helfen? Vielleicht wenn
    Sie einen Keks gehabt hatten, ware es nie dazu gekommen.

#### Piping from a file outputting to *output_file* ####

    $ java -jar clank.jar output_file < input_file

##### Output (*stderr*) #####

    | Ambiguity[output_file](2,23) |  < hätten | hatten >
    | Ambiguity[output_file](2,31) |  < wäre | Ware >

##### Output (*output_file*) #####

    Mögen Sie Glück? Können Sie vielleicht helfen? Vielleicht wenn
    Sie einen Keks gehabt hatten, ware es nie dazu gekommen.

## License ##

Copyright (C) 2010 brx

Distributed under the GPLv3. See COPYING.
