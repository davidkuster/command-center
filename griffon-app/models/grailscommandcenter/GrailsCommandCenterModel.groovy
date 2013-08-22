package grailscommandcenter

import groovy.beans.Bindable


class GrailsCommandCenterModel {

	def eventPublishService

	
	@Bindable String windowTitle = 'Command Center'
	//@Bindable String promptText = "grails"
	// TODO: use prompt text instead of binding directly to commandPrefix...
	
	@Bindable String commandPrefix

	def userButtons
	File projectDirectory
	def manualCommands

	// internal config
	String osCommandPrefix
	def preferredSize = [725, 525]
	def location = [50, 50]
	def fontSize = 12

	// config options
	Boolean consoleSmartOutput
	def consoleMaxSize
	Boolean consoleShowCommandStartAndEnd
	Boolean consoleShowWelcomeMessage



	/**
	 * called by controller on startup.  
	 *
	 * loads remembered internal config (button configuration, window size/position, etc)
	 * from a hidden file stored in the user's home directory.  loads that as the project
	 * config if a config file is not specified as a command line parameter.
	 *
	 * if no internal config file is found (eg. this is the first run of the application)
	 * then it uses a config file from the jar's resources directory to get the ball rolling.
	 */
	public void initializeProjectConfig() {
		loadInternalConfig()

		def initialConfig = getConfigFileText()
		loadProjectConfig( initialConfig )
	}


	// TODO: look into verifying input, and notifying user if it isn't correct
	public void loadProjectConfig( configFileText ) {
		def config = JsonUtil.readStringAsJson( configFileText )

		userButtons = config['buttons']
		osCommandPrefix = config['os-command-prefix'] ?: ""
		commandPrefix = config['command-prefix'] ?: ""
		manualCommands = config['manual-commands']
		projectDirectory = new File( config['project-directory'] )
		windowTitle = "Command Center (${projectDirectory})"

		// config options
		consoleSmartOutput = config['console-smart-output']
		consoleMaxSize = config['console-max-size'] as Long ?: 0
		consoleShowCommandStartAndEnd = config['console-show-command-info']
		consoleShowWelcomeMessage = config['console-show-welcome-msg']
    }


	private void loadInternalConfig() {
		File internalConfig = getInternalConfigFile()
		if ( ! internalConfig?.exists() ) return

		def configFileText = internalConfig.text
		def config = JsonUtil.readStringAsJson( configFileText )
		preferredSize = config['preferred-size'] ?: preferredSize
		location = config['location'] ?: location
		fontSize = config['font-size'] ?: fontSize
	}


	public void saveInternalConfig() {
		def location = app.views.grailsCommandCenter.commandCenterWindow.location
    	def size = app.views.grailsCommandCenter.commandCenterWindow.size
    	def fontSize = app.views.console.consoleTextArea.font.size

    	def map = createProjectConfigMap()
    	map['preferred-size'] = [size.width as Integer, size.height as Integer]
    	map['location'] = [location.x as Integer, location.y as Integer]
    	map['font-size'] = fontSize

    	// write json config data to file
		def internalConfig = getInternalConfigFile()
		if ( ! internalConfig?.exists() ) internalConfig.createNewFile()

		internalConfig.write( JsonUtil.toJsonString( map ) )
	}


	private File getInternalConfigFile() {
		new File( System.getProperty('user.home') + System.getProperty('file.separator') + ".griffon-command-center" )
	}


	private String getConfigFileText() {
        def configFileText

        // load config file from command-line param, if specified
        if ( app.startupArgs?.size() > 0 ) {
            def configFile = new File( app.startupArgs[0] )
            if ( ! configFile?.exists() || configFile?.isDirectory() ) {
                eventPublishService.consoleWriteln( "${configFile} is not a valid file, using default config" )
                configFileText = configFile.text
            }
        }
        // if no command-line param, check for last-used config
        else {
            def configFile = getInternalConfigFile()
            if ( configFile?.exists() )
            	configFileText = configFile.text
        }

        // if no config found, use included (platform-specific) default config
        if ( ! configFileText ) {
            //def defaultConfigFile = ProcessUtil.isWindows() ? 'GrailsProjectDefault-Win32.config' : 'GrailsProjectDefault-NonWin32.config'
            def defaultConfigFile = 'GrailsProjectDefault.config'
            configFileText = getClass().getResource( '/' + defaultConfigFile ).text

            // set os-command-prefix on initial load if necessary
            if ( ProcessUtil.isWindows() ) {
            	configFileText = configFileText.replace(
            		"\"os-command-prefix\": \"\"",
            		"\"os-command-prefix\": \"cmd /c\"" )
            }
        }

        return configFileText
    }


    public String getConfigAsJson() {
    	// TODO: include config file instruction comments
    	def map = createProjectConfigMap()
    	return JsonUtil.toJsonString( map )
    }


    private Map createProjectConfigMap() {
    	def buttons = []
		app.models.each { k, v ->
    		if ( k.startsWith('buttons') ) 
				buttons << ['name':v.buttonName, 'command':v.command]
		}
		def projectDir = "${projectDirectory}".replace('\\','/')

		def manualCmdList = [""]
		def comboBoxModel = app.views.grailsCommandCenter.commandBox.model
		def size = comboBoxModel?.size ?: 0
		(0..size)?.each { num ->
			def cmd = comboBoxModel.getElementAt( num )
			if ( cmd ) manualCmdList << cmd
		}

    	def map = [:]
    	map['os-command-prefix'] = osCommandPrefix
    	map['command-prefix'] = commandPrefix
    	map['buttons'] = buttons
    	map['project-directory'] = projectDir
    	map['manual-commands'] = manualCmdList
    	// config options
		map['console-smart-output'] = consoleSmartOutput
		map['console-max-size'] = consoleMaxSize
		map['console-show-command-info'] = consoleShowCommandStartAndEnd
		map['console-show-welcome-msg'] = consoleShowWelcomeMessage
    	return map
    }

}