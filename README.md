LittleLogger
============

I created this project for two reasons:

1. I'm tired of trying to find Java/Scala `System.out.println` output on Mac OS X systems.
1. I don't like the complexity of traditional logging utilities for simple/little projects.

I do a lot of things "wrong" in the code (like using `null` values), but amazingly it still manages to work.

Caveat: This logger is not intended in any way for use in large or production applications. 
If you use it there, wow, good luck, you're on your own. (Hint: Use Grizzled-SLF4J instead.)


Usage
-----

Proper usage is shown in the *MainDriver.scala* file, but to make life easier, those file contents are repeated here:

````
object MainDriver extends App {

    val logger = LittleLogger("MainDriver")
    logger.init("/Users/Al/Projects/Scala/LittleLogger/log.out")
    
    new Foo
    new Bar

}

class Foo {    
    val logger = LittleLogger(this.getClass.getName)
    logger.log("made it to A")
    logger.log("made it to B")
}

class Bar {    
    val logger = LittleLogger("Bar")
    logger.log("made it to A")
    logger.log("made it to B")
}
````

As shown, there is only a `log` method; there are no `debug`, `info`, `error` methods, etc. (Just trying to keep things simple.)

If all of that seems to make sense, give this logger a try in your own small/little applications.


Contact
-------

You can contact me at http://alvinalexander.com or http://valleyprogramming.com.
