LittleLogger
============

I created this project for two reasons:

1. I'm tired of trying to find Java/Scala `System.out.println` output on Mac OS X systems.
1. I don't like the complexity of traditional logging utilities for simple/little projects.

I do a lot of things "wrong" in the code (like using `null` values), but amazingly it still manages to work.

Caveat: This logger is not intended in any way for use in large or production applications. 
If you use it there, wow, good luck, you're on your own. (Hint: Use Grizzled-SLF4J instead.)


Changelog
---------

2104-10-13: v0.3: Changed the API so consumers can call `info`, `debug`, etc. Also, now writing to file in a Future.
2014-09-20: v0.2: Added `enabled` setting so I can easily enable/disable logging.


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

You can also disable and enable logging now if you want/need to. My assumption is that I want logging enabled
by default, so that's how it works. Call `logger.setEnabled(false)` to disable logging.

If all of that seems to make sense, give this logger a try in your own small/little applications.


For Programmers
---------------

A few notes:

1. I specifically didn't use an actor-based solution here because that has a side effect of keeping
   the application alive until you explicitly shutdown the actor system. I didn't want the consumer
   of this utility to have to know that an actor system is used, and definitely didn't want them to 
   have to worry (or think about) having to "shut down" the logger.
1. I use a `Future` in the logging process to put the file-writing on another thread. That could
   probably be done with a thread as well, but in general, I'm trying to trade in threads for futures.


Contact
-------

You can contact me at http://alvinalexander.com or http://valleyprogramming.com.
