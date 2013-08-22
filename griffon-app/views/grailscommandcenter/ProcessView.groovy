package grailscommandcenter


processStatus = hbox() {
    label( id:'statusLabel', text:bind {model.statusText} )
    
    // hSpacer() - any way to reference main view script from here?
    rigidArea( width:2 )

    label( id:'pidLabel', text:bind {model.pid} )
}
