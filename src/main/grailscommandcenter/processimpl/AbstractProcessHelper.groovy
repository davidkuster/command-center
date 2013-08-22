package grailscommandcenter.processimpl


abstract class AbstractProcessHelper {


	int getPid( Process p ) {
		def pid 

		try {
			pid = getPlatformPid( p )
		}
		catch ( e ) {
			println "ProcessHelper.getPid() exception: ${e.message}"
			e.printStackTrace()
		}

		return pid
	}


	String terminate( Process p ) {
		if ( ! p ) {
			return "Cannot terminate null process"
		}

		def status = new StringBuffer()
		def pid = getPid( p )

		try {
			terminatePlatformPid( pid, status )
		}
		catch ( e ) {
			status.append( "\nterminate exception: ${e.message}" ) 
			println "ProcessHelper.terminate() exception: ${e.message}"
			e.printStackTrace()
		}

		return status.toString()
	}


	abstract int getPlatformPid( Process p )
	abstract void terminatePlatformPid( pid, status )

	// TODO: look at making this return a list
	abstract String getPlatformChildProcesses( pid )


}

