package grailscommandcenter.processimpl

import java.lang.reflect.Field

/**
 * Windows-specific helper class
 */
class ProcessHelperMac extends AbstractProcessHelper {
	

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
    		"kill -9 ${childProcess}".execute()
    	}
    	else {
    		status.append( "no child process found for pid ${pid}, killing ${pid} instead" )
    		"kill -9 ${pid}".execute()
    		status.append( "\ncalled 'kill -9 ${pid}'" )
    	}
	}


	String getPlatformChildProcesses( pid ) {
		"ps aux | grep ${pid} | grep -v grep".execute().text
	}

}