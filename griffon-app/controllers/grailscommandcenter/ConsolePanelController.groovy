package grailscommandcenter

import java.awt.Font
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection


class ConsolePanelController {
    // these will be injected by Griffon
    def model
    def view

    // clipboard for copy (no paste)
    Clipboard clipboard = Toolkit.defaultToolkit.systemClipboard



    // startup
    void mvcGroupInit(Map args) {
        // TODO: add config option as to whether this should be shown at startup
        println "console args = ${args}"
        if ( args.showWelcomeMsg )
            model.output = getClass().getResource('/startup.txt').text
    }


    /**** console actions ****/

    def onConsoleFontSizeIncrease() {
        updateFontSize( ++view.consoleTextArea.font.size )
    }

    def onConsoleFontSizeDecrease() {
        updateFontSize( --view.consoleTextArea.font.size )
    }

    def onClearConsole() {
        currentLine = ''
        lastLine = ''
        resetOutput( '' )
        view.clearConsoleButton.requestFocusInWindow()
    }
    def onCopyConsole() {
        def text = view.consoleTextArea?.selectedText ?: model.output
        clipboard.setContents( new StringSelection( text ), null )
    }
    def onFindInConsole() {
        view.searchBar.installer.openSearchBar( view.searchBar )
        view.searchBar.focusSearchField()
    }

    def onConsoleWrite( s ) {
        consoleWrite( s )
    }
    def onConsoleWriteln( s ) {
        consoleWriteln( s )
    }

    /**** end console actions ****/


    /**** helper methods ****/

    def lastLine = ''
    def currentLine = ''

    // TODO: add option to stop automatic scrolling of panel

    private def consoleWrite( String s ) {
        currentLine += s

        // only write to console when a newline char exists
        if ( currentLine.contains('\n') || currentLine.contains('\r')
            // or a question is being asked, perhaps requiring user input...
            || currentLine.contains('?') ) {

            if ( app.models.grailsCommandCenter.consoleSmartOutput ) {
                // TODO: move this to ConsolePanelModel

                // TODO: clean this up.  removeAll compares unique characters, regardless of how many occurrences of each character are in the string.  plus this method just generally smells bad.  blech.
                // test with a small max output (200-500 chars) and 'grails stats' to see instances of incorrect output.
                def diffList = currentLine as List
                diffList?.removeAll( lastLine as List )

                if ( currentLine.startsWith('|')
                    && ( currentLine.length() - lastLine.length() <= 1 )
                    && ( diffList.size() <= 1 ) )
                {
                    // only one char diff between new line and last line
                    // so remove old line and print new line in its place
                    def index = model.output.lastIndexOf( lastLine )
                    if ( index > 0 ) {
                        def notLastLine = model.output.substring( 0, index )
                        resetOutput( notLastLine + currentLine )
                    }
                    else {
                        lastLine = ''
                        addToOutput( currentLine )
                    }
                }
                else {
                    // more than one char diff between new line and last line
                    // so just add new line to output
                    addToOutput( currentLine )
                }

                // if statement necessary for windows, for some reason
                if ( currentLine != '\n' && currentLine != '\r' )
                    lastLine = currentLine
            }
            else {
                // not doing smart output, just add line to console
                addToOutput( currentLine )
            }

            currentLine = ''
        }
    }

    private def consoleWriteln( s ) {
        consoleWrite( s + "\n" )
    }


    private def updateFontSize( size ) {
        def newFont = new Font(Font.MONOSPACED, Font.PLAIN, size)
        view.consoleTextArea.setFont( newFont )
    }


    private void addToOutput( s ) {
        def maxSize = app.models.grailsCommandCenter.consoleMaxSize as int
        // TODO: move this setting to ConsolePanelModel

        // if max length is not set to 0 then limit output
        if ( maxSize ) {
            def currentSize = model.output?.length() ?: 0
            def trimIndex = currentSize - maxSize
            if ( trimIndex > 0 ) {
                resetOutput( model.output.substring( trimIndex ) )
                return
            }
        }

        // if output wasn't reset above, add to model.output
        execInsideUISync {
            model.output += s
        }
    }

    private void resetOutput( s ) {
        execInsideUISync {
            model.output = s
        }
    }

    /**** end helper methods ****/

}
