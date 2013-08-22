package grailscommandcenter

import java.awt.event.MouseEvent


userButton = button( 
				id:model.buttonId, 
				text:bind{model.buttonName},
				toolTipText:bind{model.command},
				enabled:bind{model.enabled},
				actionPerformed:controller.buttonClick )

noparent {

	/*userButton.addMouseListener( { evt ->
		if ( evt.id == MouseEvent.MOUSE_CLICKED 
			&& ( evt.button == MouseEvent.BUTTON2 
				|| evt.button == MouseEvent.BUTTON3 ) ) {
            	// on right click, edit button config
				controller.rightClick()
			}
		}
	} as MouseListener )*/

	// got the above code working, but wondering if it will be a bit less performant by checking that the ID is MOUSE_CLICKED on every mouse over/entry/leave/etc.  the code below is a bit too magical for me, but going with it anyway.

	// btw, SwingUtilities.isRightMouseButton() wasn't working, hence checking evt.button.

	// found this at: http://griffon-user.3225736.n2.nabble.com/Showing-a-new-dialog-when-double-clicking-a-JTable-row-td7054836.html
	// that is some automagical shit.
	bean( userButton, 
		mouseClicked: { evt -> 
			if ( evt.button == MouseEvent.BUTTON2 || evt.button == MouseEvent.BUTTON3 ) {
            	// on right click, edit button config
				controller.rightClick()
			}
		} )

}