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

