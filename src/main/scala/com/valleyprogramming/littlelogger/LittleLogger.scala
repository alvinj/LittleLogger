package com.valleyprogramming.littlelogger

import java.io._
import java.util.Calendar
import java.text.SimpleDateFormat
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
//import scala.concurrent.ExecutionContext.Implicits.global

/**
 * A simple logger that lets you easily log output to a file.
 * I created this for small projects; you probably don't want to use it in
 * production projects, and definitely not in large projects.
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
class LittleLogger(identifier: String) {
    
    /**
     * I'm experimenting with getting some control over how many threads are used
     * by my futures. Found this config setting here:
     * stackoverflow.com/questions/15285284/how-to-configure-a-fine-tuned-thread-pool-for-futures
     */
    //implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))
    
    import LittleLogger.{INFO, DEBUG, WARN, ERROR}
    
    // see http://alvinalexander.com/java/simpledateformat-convert-date-to-string-formatted-parse for more
    // time formatting options
    val timeFormatter = new SimpleDateFormat("hh:mm:ss:SSS")
    
    /**
     * Initialize the output log file. This filename is used by all LittleLogger instances.
     */
    def init(filename: String, desiredLogLevel: Int) { 
        LittleLogger.filename = filename
        LittleLogger.logLevel = desiredLogLevel
        val file = new File(filename)
        file.createNewFile
    }

    def info(msg: String) {
        if (LittleLogger.logLevel <= INFO) logMessageIfEnabled(msg, "INFO")
    }

    def debug(msg: String) {
        if (LittleLogger.logLevel <= DEBUG) logMessageIfEnabled(msg, "DEBUG")
    }
    
    def warn(msg: String) {
        if (LittleLogger.logLevel <= WARN) logMessageIfEnabled(msg, "WARN")
    }
    
    def error(msg: String) {
        if (LittleLogger.logLevel <= ERROR) logMessageIfEnabled(msg, "ERROR")
    }
    
    /**
     * If logging is enabled, write the message to the output log file.
     */
    private def logMessageIfEnabled(msg: String, logLevelAsString: String) {
        if (!LittleLogger.enabled) return
        if (LittleLogger.filename == null) {
            // try to help the user by writing this log message, which hopefully they will find.
            // TODO could also throw an exception here.
            System.err.println("LittleLogger: `filename` was null, not going to write anything")
            return
        }

// 1 - Thread        
//        new Thread {
//            override def run {
//                val bw = new BufferedWriter(new FileWriter(new File(LittleLogger.filename), true))
//                bw.write(f"$getTime | $logLevelAsString%-5s | $identifier | $msg\n")
//                bw.close
//            }
//        }.start

// 2 - Future
        // i don't know why, but Future keeps the lines in the log file more or less in sync
        // from a time perspective, but the Thread solution above writes them in a random order.
//        val task = Future {
//            val bw = new BufferedWriter(new FileWriter(new File(LittleLogger.filename), true))
//            bw.write(f"$getTime | $logLevelAsString%-5s | $identifier | $msg\n")
//            bw.close
//        }

        // 3 - don't try to write in parallel
        val bw = new BufferedWriter(new FileWriter(new File(LittleLogger.filename), true))
        bw.write(f"$getTime | $logLevelAsString%-5s | $identifier | $msg\n")
        bw.close

    }
    
    private def getTime = timeFormatter.format(Calendar.getInstance.getTime)

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
        LittleLogger.enabled = enabled
    }
    
    def isEnabled = LittleLogger.enabled
}

/**
 * The basic idea here is to share the `filename` and `enabled` settings between all instances of the LittleLogger class.
 * This object also makes it a little easier to construct a LittleLogger instance with the use
 * of an `apply` method.
 */
object LittleLogger {
    
    private var filename: String = _
    private var enabled = true
    protected var logLevel = DEBUG
    
    val INFO    = 1
    val DEBUG   = 2
    val WARN    = 3
    val ERROR   = 4
    
    def apply(identifier: String): LittleLogger = {
        new LittleLogger(identifier)
    }
    
}










