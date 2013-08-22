package grailscommandcenter


build(ActionsConsole)



statusAndConsoleActionPanel = hbox() {
	//hSpacer()
    //label( id:'commandLabel', text:bind {model.command} )
    hSpacer()
    label( id:'statusLabel', text:bind {model.statusText} )
    hSpacer()
    label( id:'pidLabel', text:bind {model.pid} )
    hglue()
    button( id:'clearConsoleButton', clearConsoleAction, toolTipText:'Clear Console' )
    hSpacer()
    button( copyConsoleAction, toolTipText:'Copy Console Text' )
    hSpacer()
    button( findInConsoleAction, toolTipText:'Find in Console' )
    hSpacerEdge()
}