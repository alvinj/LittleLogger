package com.valleyprogramming.littlelogger

/**
 * Output from this example/driver program looks like this:
 * 
 *     com.valleyprogramming.littlelogger.Foo | 10:22:01:065 | made it to A
 *     com.valleyprogramming.littlelogger.Foo | 10:22:01:065 | made it to B
 *     Bar | 10:22:01:066 | made it to A
 *     Bar | 10:22:01:066 | made it to B
 * 
 */
object MainDriver extends App {

    val logger = LittleLogger("MainDriver")
    logger.init("/Users/Al/Projects/Scala/LittleLogger/log.out", LittleLogger.DEBUG)
    
    new Foo
    new Bar
    new Baz

}

class Foo {
    val logger = LittleLogger(this.getClass.getName)
    logger.debug("made it to A")
    logger.info("made it to B")
}

class Bar {    
    val logger = LittleLogger("Bar")
    logger.warn("made it to A")
    logger.error("made it to B")
}

class Baz {    
    val logger = LittleLogger("Baz")
    logger.info("made it to A")
    logger.setEnabled(false)
    logger.debug("made it to B (should not see this)")
    logger.setEnabled(true)
    logger.error("made it to C")
}

