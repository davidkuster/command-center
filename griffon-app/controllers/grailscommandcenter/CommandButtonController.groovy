package grailscommandcenter

import com.jidesoft.popup.JidePopup
import com.jidesoft.swing.*

import javax.swing.*
import javax.swing.border.TitledBorder
import java.awt.*


class CommandButtonController {

    def eventPublishService

    // these will be injected by Griffon
    def model
    def view

    // popup to edit button config
    def popup
    def nameField
    def cmdField


    //void mvcGroupInit(Map args) { }

    //void mvcGroupDestroy() { }

    /*
        Remember that actions will be called outside of the UI thread
        by default. You can change this setting of course.
        Please read chapter 9 of the Griffon Guide to know more.
    */

    /*
    TODO: look at allowing buttons to take a parameter (or multiple parameters),
    so button can be clicked on and the user enters the parameter.  example:

    button command: grails create-domain-class com.company.app.package.$1
    user enters: MyDomain
    */

    // on normal (left) click, run command
    def buttonClick = {
        eventPublishService.executeCommand( model.command, view[model.buttonId], model.mvcId )
    }

    // on right/middle click, edit button config
    def rightClick = {
        if ( ! popup ) initPopup()
        togglePopupVisibility()
    }


    // TODO: would it make sense to let each button accept multiple commands, to be executed in order?  so each could be their own little script?
    // so could easily make a clean & compile button:
    // grails clean
    // grails compile
    // grails run-app (if you wanted to run after immediately)
    // hmmm...

    // if i did that, also create option to clear console output so buttons could do:
    // clear (or cls)
    // grails test-app unit:spock...etc

    private def initPopup() {
        nameField = new JTextField( text:model.buttonName, toolTipText:"Set Button Name",
            font:new Font(Font.SANS_SERIF, Font.PLAIN, 12) )
        cmdField = new JTextField( text:model.command, toolTipText:"Set Button Command",
            font:new Font(Font.MONOSPACED, Font.PLAIN, 12) )

        popup = new JidePopup(owner:view[model.buttonId], resizable:true, movable:true, focusable:true)

        popup.contentPane.layout = new BorderLayout()
        popup.contentPane.border = BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true),
                "Edit Button Config",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 4, 4, 4) )

        def panel = Box.createVerticalBox()
        panel.add( nameField )
        panel.add( cmdField )

        def saveButton = new JButton( text:"Save", actionPerformed:editButton )
        def cancelButton = new JButton( text:"Cancel", actionPerformed:cancelButton )
        def deleteButton = new JButton( text:"Delete", actionPerformed:deleteButton )

        def buttonPanel = Box.createHorizontalBox()
        buttonPanel.add( saveButton )
        buttonPanel.add( cancelButton )
        buttonPanel.add( deleteButton )
        panel.add( buttonPanel )

        popup.contentPane.add( panel );
    }

    private def togglePopupVisibility() {
        if (popup.isPopupVisible()) {
            popup.hidePopup();
            popup = null
        }
        else {
            def button = view[model.buttonId]
            int x = button.getLocationOnScreen().x
            int y = button.getLocationOnScreen().y + button.height
            popup.showPopup( x, y );

            cmdField.requestFocusInWindow()
        }
    }


    private def editButton = {
        togglePopupVisibility()
        model.buttonName = nameField.text.trim()
        model.command = cmdField.text.trim()
    }

    private def cancelButton = {
        togglePopupVisibility()
    }

    private def deleteButton = {
        // TODO: if ( prompt: are you sure? ) - maybe...
        togglePopupVisibility()
        eventPublishService.removeUserButton( view[model.buttonId], model.mvcId )
    }

}
