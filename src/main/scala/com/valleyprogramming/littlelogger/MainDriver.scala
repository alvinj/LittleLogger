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
object MainDriver extends App {

    // create a LittleLogger instance
    val logger = LittleLogger("MainDriver")
    
    // initialize the desired log file, as well as the desired logging level
    logger.init(filename = "/Users/Al/Projects/Scala/LittleLogger/log.out", 
                desiredLogLevel = LittleLogger.INFO)
    
    val foo = new Thread {
        override def run {
            new Foo
        }
    }
    foo.start
    Thread.sleep(10)
 
    val bar = new Thread {
        override def run {
            new Bar
        }
    }
    bar.start
    Thread.sleep(10)
    
    val baz = new Thread {
        override def run {
            new Baz
        }
    }
    baz.start
    
//    import scala.concurrent.Future
//    import scala.concurrent.ExecutionContext.Implicits.global
//    val foo = Future { new Foo }
//    val bar = Future { new Bar }
//    val baz = Future { new Baz }
    
    Thread.sleep(2000)

}

class Boom {
    val logger = LittleLogger("Boom")
    logger.info("(3) this is an info message from Boom")
}

class Foo {
    //val logger = LittleLogger(this.getClass.getName)
    val logger = LittleLogger("FOO")
    logger.info("(1) this is an info message from class Foo")
    logger.debug("(2) this is a debug message from class Foo")
    new Boom
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

class Bar {    
    val logger = LittleLogger("Bar")
    Thread.sleep(10)
    logger.info("this is an info message from class Bar")
    logger.debug("this is a debug message from class Bar")
    Thread.sleep(10)
    logger.warn("this is a warn message from class Bar")
    logger.error("this is an error message from class Bar")
}

class Baz {    
    val logger = LittleLogger("BAZ")
    Thread.sleep(10)
//    logger.setEnabled(false)
    logger.info("this is an info message from class Baz")
    logger.debug("this is a debug message from class Baz")
    Thread.sleep(10)
//    logger.setEnabled(true)
    logger.warn("this is a warn message from class Baz")
    logger.error("this is an error message from class Baz")
}
