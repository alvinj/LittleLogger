package com.valleyprogramming.littlelogger

import java.io._
import java.util.Calendar
import java.text.SimpleDateFormat
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

/**
 * A simple logger that lets you easily log output to a file.
 * I created this for small projects; you probably don't want to use it in
 * production projects, and definitely not in large projects.
 * 
 * -------------------------------------------------------------------------------
 * NOTE: This class/object works with an Akka Actor to log messages on a different
 * thread. One drawback to this approach is that you need to make sure you
 * explicitly call `System.exit()` in your program, as the object here
 * listens for that event, and shuts down that ActorSystem at that time. 
 * -------------------------------------------------------------------------------
 * 
 * For the consumer, usage looks as follows. Your `main` class should create a
 * LittleLogger instance and initialize it, like this:
 * 
 *     val logger = LittleLogger("MyMainClass")
 *     logger.init("/tmp/myapp.log")
 * 
 * After that, that class can log messages like this:
 * 
 *     logger.debug(s"in controller, name = $name")
 *     logger.error("d'oh! got an error.")
 *
 * Once the logger has been initialized in your main class, other classes just need
 * to create a logger instance, and write to it, like this:
 * 
 *     val logger = LittleLogger("GuiController")
 *     logger.debug("just created the main window")
 *
 * The logger supports four logging levels, and their order is: INFO, DEBUG, WARN, ERROR.
 * If you set the logging level to INFO, it will print all messages.
 * If you set the logging level to DEBUG, it will print DEBUG, WARN, and ERROR messages.
 * 
 *  See the MainDriver.scala file that accompanies this file for more examples.
 *
 *  @param identifier The name you want to precede your output with. Typically this is the
 *  name of your class (but you can use any string).
 */
class LittleLoggerPar(identifier: String) {
    
    /**
     * I'm experimenting with getting some control over how many threads are used
     * by my futures. Found this config setting here:
     * stackoverflow.com/questions/15285284/how-to-configure-a-fine-tuned-thread-pool-for-futures
     */
    //implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))
    
    import LittleLoggerPar.{INFO, DEBUG, WARN, ERROR}
    
    /**
     * Initialize the output log file. This filename is used by all LittleLogger instances.
     */
    def init(filename: String, desiredLogLevel: Int) {
        LittleLoggerPar.init(filename, desiredLogLevel)
        LittleLoggerPar.logLevel = desiredLogLevel
    }

    def info(msg: String) {
        if (LittleLoggerPar.logLevel <= INFO) LittleLoggerPar.logMessageIfEnabled(msg, "INFO", identifier)
    }

    def debug(msg: String) {
        if (LittleLoggerPar.logLevel <= DEBUG) LittleLoggerPar.logMessageIfEnabled(msg, "DEBUG", identifier)
    }
    
    def warn(msg: String) {
        if (LittleLoggerPar.logLevel <= WARN) LittleLoggerPar.logMessageIfEnabled(msg, "WARN", identifier)
    }
    
    def error(msg: String) {
        if (LittleLoggerPar.logLevel <= ERROR) LittleLoggerPar.logMessageIfEnabled(msg, "ERROR", identifier)
    }
    
    /**
     * If logging is enabled, write the message to the output log file.
     */
    /**
     * Set this to `false` if you want to disable logging in your application.
     * For instance, you may want to do this in the `main` method of your application
     * to turn off logging when you go into production ... that's probably the biggest
     * reason. This method is really a convenience method intended for that sort of use.
     * 
     * Setting this to `true` or `false` has a global effect. If you set it to `false`
     * in your `main` method, it turns off logging throughout your application.
     * 
     * I don't recommend setting this to `false` and then back to `true` within your
     * application. This class writes to the logging file using threads, and a global
     * on/off switch combined with threads will yield unpredictable results, and to be
     * clear, that's not why this method exists; it exists for the reason stated above,
     * to be able to turn off logging globally in a `main` method, such as when you
     * go into production.
     */
    def setEnabled(enabled: Boolean) {
        LittleLoggerPar.enabled = enabled
    }
    
    def isEnabled = LittleLoggerPar.enabled
}

/**
 * The basic idea here is to share the `filename` and `enabled` settings between all instances of the LittleLogger class.
 * This object also makes it a little easier to construct a LittleLogger instance with the use
 * of an `apply` method.
 */
object LittleLoggerPar {
    
    val actorSystem = ActorSystem("LittleLoggerSystem")
    val logWriter = actorSystem.actorOf(Props[LogWriter], name = "logWriter")

    Runtime.getRuntime.addShutdownHook(new Thread {
        override def run {
            actorSystem.shutdown
        }
    })
    
    private var enabled = true
    protected var logLevel = INFO

    val INFO    = 1
    val DEBUG   = 2
    val WARN    = 3
    val ERROR   = 4

    def apply(identifier: String): LittleLoggerPar = {
        new LittleLoggerPar(identifier)
    }

    /**
     * Initialize the output log file. This filename is used by all LittleLogger instances.
     */
    protected def init(filename: String, desiredLogLevel: Int) {
        logWriter ! Init(filename)
        logLevel = desiredLogLevel
    }

    protected def logMessageIfEnabled(msg: String, logLevelAsString: String, identifier: String) {
        if (!enabled) return
        val time = getTime
        logWriter ! MessageToLog(msg, time, logLevelAsString, identifier)
    }
    
    // see http://alvinalexander.com/java/simpledateformat-convert-date-to-string-formatted-parse for more
    // time formatting options
    val timeFormatter = new SimpleDateFormat("hh:mm:ss:SSS")
    
    private def getTime = timeFormatter.format(Calendar.getInstance.getTime)

}










