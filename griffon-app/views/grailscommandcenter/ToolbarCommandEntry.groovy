package grailscommandcenter


// create actions used by this script
build(ActionsCommandEntry)


commandEntryBox = hbox() {
	hSpacerEdge()
    label( text:bind{model.commandPrefix} ) // promptText} ) 
    label( text:'>', font:new Font(Font.MONOSPACED, Font.BOLD, 10) )
    hSpacer()
    autoCompletionComboBox( id:'commandBox', editable:true, strict:false,
        items:model.manualCommands )
    hSpacer()
    button( id:'stopButton', stopAction, toolTipText:'Terminate Current Process',
    	enabled:false ) // default to disabled
    hSpacer()
    button( id:'createNewButton', createNewButtonAction, toolTipText:'Create New Command' )
    hSpacerEdge()
}

noparent {
    // put action listener on combo box's editor so only enter key triggers the manual command
    commandBox.editor.addActionListener( controller.manualCommand as ActionListener )
} 


return commandEntryBox