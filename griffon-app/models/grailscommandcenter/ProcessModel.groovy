package grailscommandcenter

import groovy.beans.Bindable

class ProcessModel {

	// states
	// TODO move these to an enum - ProcessStatus.RUNNING, etc
	public final int RUNNING = 0
	public final int COMPLETE = 1
	public final int STOPPED = 2
	public final int CLEARED = 3
   
	@Bindable String pid = ''
	@Bindable String currentCommand = ''
	@Bindable boolean isRunning = false
	@Bindable int status = COMPLETE
	@Bindable String statusText = ''
	private Process process
	


	void setStatus( int newStatus ) {
		status = newStatus

		def newStatusText
		switch ( status ) {
			case RUNNING:
				newStatusText = "Command running..."
				break
			case COMPLETE:
				newStatusText = "Execution complete."
				break
			case STOPPED:
				newStatusText = "Execution halted."
				break
			case CLEARED:
				newStatusText = ''
				break
		}

		firePropertyChange("statusText", this.statusText, this.statusText = newStatusText)
	}


	boolean isRunning() {
		status == RUNNING
	}

}