package com.valleyprogramming.littlelogger

import java.io._
import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * A very simple logger that lets you easily log output to a file.
 * 
 * Order of the logging levels is: INFO, DEBUG, WARN, ERROR.
 * If you set the logging level to INFO, it will print all messages.
 * If you set the logging level to DEBUG, it will print DEBUG, WARN, and ERROR messages.
 * 
 * Usage:
 *     val logger = LittleLogger("MyClass")
 *     logger.init("/tmp/myapp.log")
 *     logger.debug("made it to A")
 *     logger.warn("made it to B")
 *
 *  See the MainDriver.scala file that accompanies this file for more examples.
 *
 *  @param identifier The name you want to precede your output with. Typically the name of your class.
 */
class LittleLogger(identifier: String) {
    
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
            System.err.println("LittleLogger: `filename` was null, not going to write anything")
            return
        }
        val bw = new BufferedWriter(new FileWriter(new File(LittleLogger.filename), true))
        bw.write(s"$getTime | $logLevelAsString | $identifier | $msg\n")
        bw.close
    }
    
    private def getTime = timeFormatter.format(Calendar.getInstance.getTime)

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
    
    val DEBUG   = 1
    val INFO    = 2
    val WARN    = 3
    val ERROR   = 4
    
    def apply(identifier: String): LittleLogger = {
        new LittleLogger(identifier)
    }
    
}










