package grailscommandcenter.processimpl

import java.io.IOException
import java.lang.reflect.Field
import static com.sun.jna.platform.win32.WinDef.DWORD


/**
 * Windows-specific helper class
 */
class ProcessHelperWindows extends AbstractProcessHelper {
	

	int getPlatformPid( Process p ) {
		Field f = p.getClass().getDeclaredField("handle")
		f.setAccessible(true)
		int pid = JnaWrapper.INSTANCE.GetProcessId((Long) f.get(p))
		return pid
	}


	void terminatePlatformPid( pid, status ) {
		def handle = openHandleToPid( pid )

		if ( ! handle ) {
			status.append "unable to get handle to pid ${pid}: ${getLastError()}"
		}
		else {
			// get list of children
			def children = getWindowsChildProcesses( handle, pid )

			// terminate parent
            boolean b = terminateWindowsProcess( handle )
            if ( b ) status.append( "terminate successful" )
            else status.append( "\nterminate failed: ${getLastError()}" )

            // terminate children
            children?.each { childHandle ->
            	boolean b2 = terminateWindowsProcess( childHandle )
            	if ( ! b2 ) status.append( "\nchild terminate failed: ${getLastError}" )
            }
		}
	}


	String getPlatformChildProcesses( pid ) {
		def c = new StringBuffer()

		def handle = openHandleToPid( pid )
		def children = getWindowsChildProcesses( handle, pid )
		children?.each { c.append( it ) }

		return c.toString()
	}


	private def getLastError() {
		"error code [${JnaWrapper.INSTANCE.GetLastError()}]"
	}


	private def openHandleToPid( pid ) {
		JnaWrapper.INSTANCE.OpenProcess( JnaWrapper.ACCESS_OPTIONS, false, pid )
	}

	private def terminateWindowsProcess( handle ) {
		//println "terminating process ${handle}"
		JnaWrapper.INSTANCE.TerminateProcess( handle, 0 )
		// TODO: figure out jna*.dll temp files getting created.  after code is open sourced post a msg to the JNA users google group - jna-users@googlegroups.com
		// also think about deleting old temp files on startup - potentially get system temp folder via System.getProperty('java.io.tmpdir') (haven't tested that works)
	}


	private def getWindowsChildProcesses( handle, pid ) throws IOException {
		def children = []

		def hSnap = JnaWrapper.INSTANCE.CreateToolhelp32Snapshot( JnaWrapper.TH32CS_SNAPPROCESS, new DWORD(0) )
		def ent = new JnaWrapper.PROCESSENTRY32.ByReference()

		if ( JnaWrapper.INSTANCE.Process32First( hSnap, ent ) ) {
			if ( ent.th32ParentProcessID.intValue() == pid ) {
				//println "adding child process ${ent.th32ProcessID} to list"
				children << openHandleToPid( ent.th32ProcessID.intValue() )
			}

			while ( JnaWrapper.INSTANCE.Process32Next( hSnap, ent ) ) {
				if ( ent.th32ParentProcessID.intValue() == pid ) {
					//println "adding child process ${ent.th32ProcessID} to list"
					children << openHandleToPid( ent.th32ProcessID.intValue() )
				}
			}
		}

		return children
	}


}