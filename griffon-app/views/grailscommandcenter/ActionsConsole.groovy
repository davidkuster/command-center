package grailscommandcenter


actions {
	// console actions
    action(
        id: 'clearConsoleAction',
        name: 'Clear',
        closure: { app.serviceManager.findService('eventPublishService').clearConsole() } )
    action(
        id: 'copyConsoleAction',
        name: 'Copy',
        closure: { app.serviceManager.findService('eventPublishService').copyConsole() } )
    action(
        id: 'findInConsoleAction',
        name: 'Find',
        closure: { app.serviceManager.findService('eventPublishService').findInConsole() } )
    // end console actions
}