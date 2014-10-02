package gui.console

import groovy.transform.Immutable

@Immutable
class ConsoleViewEntry {
    String level
    String logger
    String message
    Date timeStamp
}
