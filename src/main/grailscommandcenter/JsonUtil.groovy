package grailscommandcenter

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.json.JsonOutput


class JsonUtil {


	/**
	 * reads a string into a JSON object, removing any lines that start with //
	 */
	static def readStringAsJson( String s ) {
		def lines = []

		s?.eachLine { line ->
			// strip comments
			def i = line.indexOf('//')
			if ( i != -1 ) {
				line = line.substring( 0, i )
			}
			// add line if it has any data
			if ( line )
				lines << line 
		}

		def json = new StringBuilder()
		lines?.each { json.append( it ) }

		def slurper = new JsonSlurper()
		return slurper.parseText( json.toString() )
	}


	/**
	 * takes a map that has been configured to mimic the desired json structure
	 * and converts it into a string of pretty-printed JSON data.
	 */ 
	static String toJsonString( Map map ) {
		def builder = new JsonBuilder( map )
		def json = builder.toString()
		def pretty = JsonOutput.prettyPrint( json )
		// bug in ubuntu - "" getting turned into "\"\"" - revert that if necessary
		return pretty.replace( '\\"\\"', '' )
	}

}