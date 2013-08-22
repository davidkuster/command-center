package grailscommandcenter


/**
 * using a service to call app.event() so event names aren't hardcoded all
 * over the application, and i can see all the events in use in one place.
 * plus, it just smells bad to interact with that plumbing directly.
 */
class EventPublishService {

	/**** process events ****/

    def executeCommand( command, actionButton, buttonMvcId ) {
    	app.event( 'executeCommand', [command, actionButton, buttonMvcId] )
    }

    def commandComplete( actionButton ) {
    	app.event( 'commandComplete', [actionButton] )
    }

    def stopProcess() {
    	app.event( 'stopProcess' )
    }

    /**** end process events ****/


    /**** UI events ****/

    def removeUserButton( actionButton, buttonMvcId ) {
    	app.event( 'removeUserButton', [actionButton, buttonMvcId] )
    }

    def consoleWriteln( string ) {
    	app.event( 'consoleWriteln', [string] )
    }

    def consoleWrite( string ) {
    	app.event( 'consoleWrite', [string] )
    }

    def consoleFontSizeIncrease() {
    	app.event( 'consoleFontSizeIncrease' )
    }

    def consoleFontSizeDecrease() {
    	app.event( 'consoleFontSizeDecrease' )
    }

    def clearConsole() {
    	app.event( 'clearConsole' )
    }

    def copyConsole() {
    	app.event( 'copyConsole' )
    }

    def findInConsole() {
    	app.event( 'findInConsole' )
    }

    /**** end UI events ****/

}