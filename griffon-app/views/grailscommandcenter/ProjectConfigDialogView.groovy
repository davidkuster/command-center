package grailscommandcenter


/*noparent {
    def eventPublishService = app.serviceManager.findService('eventPublishService')

    eventPublishService.consoleWriteln "commandPrefix = ${app.models.grailsCommandCenter.commandPrefix}"
    eventPublishService.consoleWriteln "consoleMaxSize = ${app.models.grailsCommandCenter.consoleMaxSize}"
    eventPublishService.consoleWriteln "consoleSmartOutput = ${app.models.grailsCommandCenter.consoleSmartOutput}"
    eventPublishService.consoleWriteln "consoleShowCommandStartAndEnd = ${app.models.grailsCommandCenter.consoleShowCommandStartAndEnd}"
    eventPublishService.consoleWriteln "consoleShowWelcomeMessage = ${app.models.grailsCommandCenter.consoleShowWelcomeMessage}"
}*/

panel( id:'content' ) {

    gridBagLayout()

    vbox(anchor: NORTH, insets: [7, 7, 7, 7]) {

        hbox() {
            label( "Command Prefix:" )
            textField( text:bind( 'commandPrefix', source:app.models.grailsCommandCenter, mutual:true ) )
            label( "(set full path if necessary)" )
        }

        hbox() {
            label( "Console Max Chars: " )
            textField( text:bind( 'consoleMaxSize', source:app.models.grailsCommandCenter, mutual:true ) )
            label( "(0 = no limiting of output)" )
        }

        checkBox( "Smart Console Output", 
            selected:bind( 'consoleSmartOutput', source:app.models.grailsCommandCenter, mutual:true ) )

        checkBox( "Show Current Command Info in Console",
            selected:bind( 'consoleShowCommandStartAndEnd', source:app.models.grailsCommandCenter, mutual:true ) )

        checkBox( "Show Welcome Message at Startup",
            selected:bind( 'consoleShowWelcomeMessage', source:app.models.grailsCommandCenter, mutual:true ) )

    }
}