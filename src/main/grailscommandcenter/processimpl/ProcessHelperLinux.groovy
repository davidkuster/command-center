package grailscommandcenter.processimpl

import java.lang.reflect.Field

/**
 * Windows-specific helper class
 */
class ProcessHelperLinux extends AbstractProcessHelper {
	

	int getPlatformPid( Process p ) {
		Field f = p.getClass().getDeclaredField("pid")
		f.setAccessible(true)
		int pid = (Integer) f.get(p)
		return pid
	}


	void terminatePlatformPid( pid, status ) {
		def childProcess = getPlatformChildProcesses( pid )

		// currently assumes only 1 child process
        if ( childProcess ) {
            status.append( "killing child process ${childProcess}" )
            "kill ${childProcess}".execute()
        }
        else {
        	// no child process, just kill current process
        	"kill ${pid}".execute()
        	// should this be kill -9?
        	// TODO: externalize these commands to a property/config file
        }
        
        // TODO: is there a way to check if the terminate actually succeeded?
        status.append( "\nterminate successful" )
	}


	String getPlatformChildProcesses( pid ) {
		"pgrep -P ${pid}".execute().text
	}

}