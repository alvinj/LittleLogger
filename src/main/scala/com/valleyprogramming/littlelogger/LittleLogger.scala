package com.valleyprogramming.littlelogger

import java.io._
import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * A very simple logger that lets you easily log output to a file.
 * 
 * Usage:
 *     val logger = LittleLogger("MyClass")
 *     logger.init("/tmp/myapp.log")
 *     logger.log("made it to A")
 *     logger.log("made it to B")
 *     
 *  See the MainDriver.scala file that accompanies this file for more examples.
 *  
 *  @param identifier The name you want to precede your output with. Typically the name of your class.
 */
class LittleLogger(identifier: String) {

    // see http://alvinalexander.com/java/simpledateformat-convert-date-to-string-formatted-parse for more
    // time formatting options
    val timeFormatter = new SimpleDateFormat("hh:mm:ss:SSS")

    /**
     * Initialize your output log file. This filename is used by all LittleLogger instances.
     */
    def init(filename: String) { 
        LittleLogger.filename = filename
        val file = new File(filename)
        file.createNewFile
    }

    /**
     * Write your message to the output log file.
     */
    def log(msg: String) {
        if (LittleLogger.filename == null) {
            System.err.println("LittleLogger: `filename` was null, not going to write anything")
            return
        }
        val bw = new BufferedWriter(new FileWriter(new File(LittleLogger.filename), true))
        bw.write(s"$identifier | $getTime | $msg\n")
        bw.close
    }
    
    private def getTime = timeFormatter.format(Calendar.getInstance.getTime)

}

/**
 * The basic idea here is to share the filename between all instances of the LittleLogger class.
 * This object also makes it a little easier to construct a LittleLogger instance with the use
 * of an `apply` method.
 */
object LittleLogger {
    
    private var filename: String = _
    
    def apply(identifier: String): LittleLogger = {
        new LittleLogger(identifier)
    }

}



