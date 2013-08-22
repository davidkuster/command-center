package grailscommandcenter

import groovy.beans.Bindable

class ConsolePanelModel {

   	@Bindable String output = ''

   	Long maxOutputSize = 100000  
   	Boolean smartOutput = true
   	// TODO: make this configurable
   	// TODO: put note in help that setting of zero doesn't limit output
	
}