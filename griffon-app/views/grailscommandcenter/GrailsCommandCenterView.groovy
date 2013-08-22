package grailscommandcenter

import javax.swing.*
import java.awt.*


// UI helpers
vSpacer = { vstrut( height:2 ) }
hSpacer = { rigidArea( width:2 ) }
hSpacerEdge = { rigidArea( width:5 ) } // wider spacer to use around window edges
hGlue = { hglue() }


commandCenterWindow = application(
    title: bind {model.windowTitle},
    preferredSize: bind {model.preferredSize},
    pack: true,
    location: bind {model.location},
    //locationByPlatform:true,
    iconImage: imageIcon('/griffon-icon-48x48.png').image,
    iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                imageIcon('/griffon-icon-32x32.png').image,
                imageIcon('/griffon-icon-16x16.png').image] )
{
    // menu bar
    widget(build(MenuBar))

    // bind escape key to clear console output
    /*keyStrokeAction(
        component:current.contentPane,
        //keyStroke:shortcut('ESCAPE'),
        keyStroke:KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, KeyEvent.CTRL_DOWN_MASK),
        action:menuClearConsoleAction )*/
    // TODO: figure out how to make this work

    // TODO: look into making UI tabbed so multiple projects can be open at once
    //tabbedPane( id:'tab', constraints:CENTER )

    borderLayout( vgap: 2, hgap: 2 )

    vbox( constraints: NORTH ) {
        // user button and command entry/stop button toolbars
        widget(build(ToolbarUserButtons))
        vSpacer()
        widget(build(ToolbarCommandEntry))
    }

    // console for process output
    scrollPane( id:'consolePanel', constraints:CENTER ) {
        widget( app.views.console.consoleTextArea )
    }

    /*box( constraints:CENTER ) {
        new com.jidesoft.editor.CodeEditor( id:'console', text:bind {model.output}, lineNumberVisible:false )
    }*/
    // TODO: look at making the console smarter than just a text area.
    // unfortunately, doesn't look like jide's code editor is open source. :(

    vbox( constraints:SOUTH ) {
        widget( app.views.console.searchBar )
        // status and clear/copy/find controls
        hbox() {
            hSpacer()
            widget( app.views.process.processStatus )
            hglue()
            widget( app.views.console.consoleButtons )
            hSpacerEdge()
        }
    }

}


// file dialog to open projects
openProjectDialog = new FileDialog( commandCenterWindow, "Open Project Config", FileDialog.LOAD )
openProjectDialog.setFile( "*.config" );
openProjectDialog.setDirectory( "." );

// file dialog to save projects
saveProjectDialog = new FileDialog( commandCenterWindow, "Save Project Config", FileDialog.SAVE )
saveProjectDialog.setFile( "*.config" );
saveProjectDialog.setDirectory( "." );

// file dialog to set project directory
projectDirectoryChooser = fileChooser()
projectDirectoryChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY )
projectDirectoryChooser.setCurrentDirectory( new File(".") );


return commandCenterWindow
