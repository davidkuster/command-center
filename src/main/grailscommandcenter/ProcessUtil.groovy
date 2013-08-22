package grailscommandcenter

import grailscommandcenter.processimpl.*
import com.sun.jna.Platform

/**
 * using code and ideas from:
 * http://stackoverflow.com/questions/4912282
 * http://stackoverflow.com/questions/10124299
 * http://www.golesny.de/p/code/javagetpid
 *
 * JNA javadoc: http://twall.github.com/jna/3.4.1/javadoc/
 *
 * implemented factory pattern and moved most code to helper classes.
 */
class ProcessUtil {


	// factory method
	static def createHelper() {
		def helper

		if ( Platform.isWindows() )
			helper = new ProcessHelperWindows()
		else if ( Platform.isLinux() )
			helper = new ProcessHelperLinux()
		else if ( Platform.isMac() ) 
			helper = new ProcessHelperMac()
		else 
			throw new UnsupportedOperationException( "Cannot create ProcessHelper for unknown platform - only Windows, Linux and Mac are supported" )

		return helper
	}


	// hide JNA
	static boolean isWindows() {
		Platform.isWindows()
	}
	static boolean isLinux() {
		Platform.isLinux()
	}
	static boolean isMac() {
		Platform.isMac()
	}

}