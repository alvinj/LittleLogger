package com.valleyprogramming.littlelogger

/**
 * Output from this example/driver program looks like this:
 * 
 *    04:52:51:541 | INFO  | Bar | this is an info message from class Bar
 *    04:52:51:541 | WARN  | Bar | this is a warn message from class Bar
 *    04:52:51:541 | ERROR | Bar | this is an error message from class Bar
 *    04:52:51:541 | WARN  | Baz | this is a warn message from class Baz
 *    04:52:51:541 | ERROR | Baz | this is an error message from class Baz
 * 
 */
object MainDriver2 extends App {

    // create a LittleLogger instance
    val logger = LittleLoggerPar("MainDriver")
  
    // initialize the desired log file, as well as the desired logging level
    logger.init(filename = "/Users/Al/Projects/Scala/LittleLogger/log.out", 
                desiredLogLevel = LittleLoggerPar.INFO)
    
    val foo = new Thread {
        override def run {
            new Foo2
        }
    }
    foo.start
    Thread.sleep(10)
 
    val bar = new Thread {
        override def run {
            new Bar2
        }
    }
    bar.start
    Thread.sleep(10)
    
    val baz = new Thread {
        override def run {
            new Baz2
        }
    }
    baz.start
    
    Thread.sleep(2000)
    System.exit(0)

}

class Boom2 {
    val logger = LittleLoggerPar("Boom")
    logger.info("(3) this is an info message from Boom")
}

class Foo2 {
    //val logger = LittleLogger(this.getClass.getName)
    val logger = LittleLoggerPar("FOO")
    logger.info("(1) this is an info message from class Foo")
    logger.debug("(2) this is a debug message from class Foo")
    new Boom2
    logger.debug("(4) this is a debug message from class Foo")
    Thread.sleep(10)
    logger.warn("this is a warn message from class Foo")
    logger.error("this is an error message from class Foo")
    Thread.sleep(10)
    logger.info("this is an info message from class Foo")
    logger.debug("this is a debug message from class Foo")
    Thread.sleep(10)
    logger.warn("this is a warn message from class Foo")
    logger.error("this is an error message from class Foo")
    Thread.sleep(10)
    logger.info("this is an info message from class Foo")
    logger.debug("this is a debug message from class Foo")
    Thread.sleep(10)
    logger.warn("this is a warn message from class Foo")
    logger.error("this is an error message from class Foo")
}

class Bar2 {    
    val logger = LittleLoggerPar("Bar")
    Thread.sleep(10)
    logger.info("this is an info message from class Bar")
    logger.debug("this is a debug message from class Bar")
    Thread.sleep(10)
    logger.warn("this is a warn message from class Bar")
    logger.error("this is an error message from class Bar")
}

class Baz2 {    
    val logger = LittleLoggerPar("BAZ")
    Thread.sleep(10)
//    logger.setEnabled(false)
    logger.info("this is an info message from class Baz")
    logger.debug("this is a debug message from class Baz")
    Thread.sleep(10)
//    logger.setEnabled(true)
    logger.warn("this is a warn message from class Baz")
    logger.error("this is an error message from class Baz")
}
