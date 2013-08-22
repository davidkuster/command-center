package grailscommandcenter

import javax.swing.*
import javax.swing.border.TitledBorder
import java.awt.*
import com.jidesoft.popup.JidePopup
import com.jidesoft.swing.*
import groovy.transform.Synchronized


class GrailsCommandCenterController {

    def eventPublishService

    // injected by Griffon
    def model
    def view

    // always increasing counter to seed mvcIds
    def buttonIndexCount = 0


    /**** lifecycle methods ****/

    // app startup
    void mvcGroupInit(Map args) {
        model.initializeProjectConfig()

        // create process & console MVC groups
        createMVCGroup( 'process', 'process' )
        createMVCGroup( 'consolePanel', 'console', [showWelcomeMsg:model.consoleShowWelcomeMessage] )
    }

    def onReadyEnd(a) {
        // setup user buttons
        initializeUserButtonDisplay()
    }

    // app shutdown
    void mvcGroupDestroy() {
        model.saveInternalConfig()
    }

    /**** end lifecycle methods ****/


    /**** view control actions ****/

    def refreshButtonView() {
        view.buttonBox.revalidate()
    }

    def initializeUserButtonDisplay() {
        // create button MVC groups
        model.userButtons?.each { config ->
            createUserButton( config.name, config.command )
        }
        refreshButtonView()
    }

    def createUserButton( buttonName, buttonCommand ) {
        def enabled = ! app.models.process.isRunning()
        def mvcId = "buttons${buttonIndexCount++}"
        def group = buildMVCGroup( 'commandButton', mvcId,
            [buttonId:mvcId, buttonName:buttonName, command:buttonCommand, order:buttonIndexCount, mvcId:mvcId, enabled:enabled] )
        // add button to main view
        view.buttonBox.add( group.view.userButton )
    }

    def onRemoveUserButton( userButton, mvcId ) {
        // remove from view
        view.buttonBox.remove( userButton )
        // destroy MVC group
        destroyMVCGroup( mvcId )
        // repaint so removal takes effect in the UI
        refreshButtonView()
        // remove from buttons config?
    }

    def createNewButton = {
        // add to user button config

        // create button
        createUserButton( 'Edit Me', '' )
        // repaint the UI so the new button appears
        refreshButtonView()
    }

    def onExecuteCommand( command, actionButton, buttonMvcId ) {
        view.commandBox.editor.item = command
        view.stopButton.enabled = true
        view.stopButton.requestFocusInWindow()

        // disable user buttons while command is executing
        app.models.findAll { k, v -> k =~ "buttons" && v.mvcId != buttonMvcId }*.value*.enabled = false

        // mark executing button
        actionButton.background = new Color(72, 118, 255)
        // TODO: make color configurable?
    }

    def onCommandComplete( actionButton ) {
        // view updates
        actionButton.requestFocusInWindow()
        actionButton.background = Color.LIGHT_GRAY
        view.commandBox.editor.item = ''
        view.commandBox.editor.editorComponent.background = Color.LIGHT_GRAY
        view.stopButton.enabled = false

        // enable user buttons after command is finished
        app.models.findAll { k, v -> k =~ "buttons" }*.value*.enabled = true
    }

    /**** end view control actions ****/




    /**** button actions ****/

    def stop = {
        eventPublishService.stopProcess()
    }

    def manualCommand = {
        def cb = view.commandBox
        def command = cb.editor.item.toString()

        if ( command ) {
            cb.editor.editorComponent.opaque = true
            cb.editor.editorComponent.background = new Color(72, 118, 255)

            if ( cb.model.getIndexOf( command ) == -1 )
                cb.model.addElement( command )

            eventPublishService.executeCommand( command, view['commandBox'], null )
        }
    }

    /**** end button actions ****/



    /**** menu actions ****/

    def openProject = {
        view.openProjectDialog.show()
        def selectedFile = view.openProjectDialog.getFile()
        if ( ! selectedFile ) return

        File configFile = new File( view.openProjectDialog.getDirectory() +
           System.getProperty("file.separator") + view.openProjectDialog.getFile() )

        // load file
        model.loadProjectConfig( configFile.text )

        // clear any existing buttons
        view.buttonBox.removeAll()

        // destroy existing button MVC groups
        app.models.each { k, v ->
            if ( k.startsWith('buttons') )
                destroyMVCGroup( v.mvcId )
        }

        // add new buttons
        initializeUserButtonDisplay()

        // update window title
        view.commandCenterWindow.title = model.windowTitle
    }

    def selectProjectDirectory = {
        def openResult = view.projectDirectoryChooser.showOpenDialog(view.commandCenterWindow)
        if(JFileChooser.APPROVE_OPTION == openResult) {
            File file = new File(view.projectDirectoryChooser.selectedFile.toString())

            model.projectDirectory = file
            view.commandCenterWindow.title = "Command Center (${file})"
        }
    }

    def closeProject = {
        //destroyMvcGroups...
    }

    def saveProject = {
        doOutside {

        }
    }

    def saveProjectAs = {
        doOutside {
            view.saveProjectDialog.show()
            def selectedFile = view.saveProjectDialog.getFile()

            eventPublishService.consoleWriteln( model.getConfigAsJson() )

            File configFile = new File( view.saveProjectDialog.getDirectory() +
               System.getProperty("file.separator") + selectedFile )

            // load file into config holder obj
            configFile.write( model.getConfigAsJson() )

            eventPublishService.consoleWriteln( "Project config saved to ${configFile}" )
        }
    }

    def quit = {
        //if ( autosave ) saveProject()
        stop()
        app.shutdown()
    }

    def editCommandHistory = {}

    def clearCommandHistory = {
        view.commandBox.model.removeAllElements()
    }

    def prefsProject = {
        def window = SwingUtilities.windowForComponent( view.commandCenterWindow )
        withMVCGroup( "projectConfigDialog" ) { m, v, c ->
            c.show(window)
        }
    }

    def prefsDisplay = {}

    def increaseFontSize = {
        eventPublishService.consoleFontSizeIncrease()
    }
    def decreaseFontSize = {
        eventPublishService.consoleFontSizeDecrease()
    }

    // TODO: create another MVC group for these popups, and perhaps the edit command popup
    def helpTutorial = {
        def text = getClass().getResource('/tutorial.html').text

        def popup = new JidePopup( resizable:true, movable:true, focusable:true )
        popup.contentPane.layout = new BorderLayout()
        popup.contentPane.border = BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true),
                "Tutorial",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 4, 4, 4) )

        def pane = new JEditorPane( contentType:'text/html', text:text, opaque:false, editable:false )
        def scrollPane = new JScrollPane( pane )

        def buttonBox = Box.createHorizontalBox()
        buttonBox.add( new JButton( text:'Close', actionPerformed:{ popup.hidePopup() } ) )

        def panel = Box.createVerticalBox()
        panel.add( scrollPane )
        panel.add( buttonBox )
        panel.setPreferredSize( new Dimension(500, 500) )

        popup.contentPane.add( panel )

        def parentLoc = view.consolePanel.getLocationOnScreen()
        int x = parentLoc.x + view.consolePanel.width / 6
        int y = parentLoc.y
        popup.showPopup( x, y )
    }

    def helpAbout = {
        def text = getClass().getResource('/about.html').text

        def popup = new JidePopup( resizable:true, movable:true, focusable:true )
        popup.contentPane.layout = new BorderLayout()
        popup.contentPane.border = BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true),
                "About",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 4, 4, 4) )

        def pane = new JEditorPane( contentType:'text/html', text:text, opaque:false, editable:false )
        def scrollPane = new JScrollPane( pane )

        def buttonBox = Box.createHorizontalBox()
        buttonBox.add( new JButton( text:'Close', actionPerformed:{ popup.hidePopup() } ) )

        def panel = Box.createVerticalBox()
        panel.add( scrollPane )
        panel.add( buttonBox )
        panel.setPreferredSize( new Dimension(500, 500) )

        popup.contentPane.add( panel )

        def parentLoc = view.consolePanel.getLocationOnScreen()
        int x = parentLoc.x + view.consolePanel.width / 6
        int y = parentLoc.y
        popup.showPopup( x, y )
    }

    // console menu actions - wrapper to console controller
    def clearConsole = {
        eventPublishService.clearConsole()
    }
    def copyConsole = {
        eventPublishService.copyConsole()
    }
    def findInConsole = {
        eventPublishService.findInConsole()
    }

    /**** end menu actions ****/


    /**** uncaught exception handling ****/

    private lastCaughtException

    @Synchronized
    void onUncaughtRuntimeException(RuntimeException e) {
        lastCaughtException = e
        println "Got a RuntimeException -> $e"
        eventPublishService.consoleWriteln( "Unhandled runtime exception: ${e}" )
    }

    @Synchronized
    void onUncaughtExceptionThrown(e) {
        if(lastCaughtException == e) return
        lastCaughtException = e
        println "OH NOES! $e"
        eventPublishService.consoleWriteln( "Unhandled exception: ${e}" )
    }


}
