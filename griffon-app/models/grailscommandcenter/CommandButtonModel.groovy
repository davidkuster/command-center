package grailscommandcenter

import groovy.beans.Bindable

class CommandButtonModel {
   
	// File projectDirectory

	@Bindable String buttonId
	@Bindable String buttonName
	@Bindable String command

	int order
	@Bindable boolean enabled = true
	String mvcId


	void mvcGroupInit( Map args ) {
		mvcId = args.mvcId
		buttonId = args.buttonId
		buttonName = args.buttonName
		command = args.command
		order = args.order
		enabled = args.enabled
	}

}