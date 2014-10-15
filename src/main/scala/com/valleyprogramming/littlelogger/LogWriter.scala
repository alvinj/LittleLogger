package com.valleyprogramming.littlelogger

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import java.io._

case class MessageToLog(msg: String, time: String, logLevelAsString: String, identifier: String)
case class Init(filename: String)

class LogWriter extends Actor {

    val logWriterHelper = context.actorOf(Props[LogWriterHelper], name = "LogWriterHelper")

    def receive = {
        case Init(filename) => logWriterHelper ! Init(filename) 
        case MessageToLog(msg, time, logLevelAsString, identifier) => logWriterHelper ! MessageToLog(msg, time, logLevelAsString, identifier)
        case _       => // ignore
    }
}


class LogWriterHelper extends Actor {

    private var filename: String = _

    def receive = {
        case Init(filename) => init(filename)
        case MessageToLog(msg, time, logLevelAsString, identifier) => logMessage(msg, time, logLevelAsString, identifier)
        case _       => // ignore
    }

    /**
     * Initialize the output log file. This filename is used by all LittleLogger instances.
     */
    def init(filename: String) {
        this.filename = filename
        val file = new File(filename)
        file.createNewFile
    }

    /**
     * If logging is enabled, write the message to the output log file.
     */
    private def logMessage(msg: String, time: String, logLevelAsString: String, identifier: String) {
        if (filename == null) {
            // try to help the user by writing this log message, which hopefully they will find.
            // TODO could also throw an exception here.
            System.err.println("LittleLogger: `filename` was null, not going to write anything")
            return
        }

        // 3 - don't try to write in parallel
        val bw = new BufferedWriter(new FileWriter(new File(filename), true))
        bw.write(f"$time | $logLevelAsString%-5s | $identifier | $msg\n")
        bw.close

    }
    
}

