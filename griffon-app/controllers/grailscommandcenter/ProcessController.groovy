package grailscommandcenter

import java.awt.Color


class ProcessController {

    def eventPublishService

    // these will be injected by Griffon
    def model
    def view

    // platform-specific process abstraction
    def processHelper

    // set by user config
    //def commandPrefix
    //def projectDirectory

    // used to interrupt console writing when stop process event is received
    static boolean writeOutputToConsole = true



    void mvcGroupInit(Map args) {
        // get user config
        //commandPrefix = args.commandPrefix
        //projectDirectory = args.projectDirectory
        // set helper class via factory method
        processHelper = ProcessUtil.createHelper()
    }

    void mvcGroupDestroy() {
        try { processHelper.terminate( model.process ) }
        catch (e) {}
    }


    def onStopProcess = { evt = null ->
        // interrupt console writing
        writeOutputToConsole = false

        if ( model.process ) {
            eventPublishService.consoleWrite( "\nstopping process..." )
            def terminateResult = processHelper.terminate( model.process )
            eventPublishService.consoleWriteln( terminateResult )

            model.process = null
            model.setStatus( model.STOPPED )
        }
    }


    def onClearConsole() {
        if ( model.status != model.RUNNING ) {
            model.setStatus( model.CLEARED )
            model.currentCommand = ''
        }
    }


    /**** common execute command action ****/

    // could also be a closure:
    // def onExecuteCommand = { actionButton, command ->

    def onExecuteCommand( command, actionButton, buttonMvcId ) {
        // TODO: think about a better way to do this - probably set commandPrefix & projectDirectory on the process model & have that updated by the parent controller when the configuration is reloaded - most likely via an event...
        def osCommandPrefix = app.models.grailsCommandCenter.osCommandPrefix
        def commandPrefix = app.models.grailsCommandCenter.commandPrefix
        def projectDirectory = app.models.grailsCommandCenter.projectDirectory
        //def promptText = app.models.grailsCommandCenter.promptText

        // verify that command can be run
        if ( ! projectDirectory?.exists() ) {
            // TODO: move error messages & whatnot to messages.properties & retreive via app.getMessage('this.is.the.code')
            eventPublishService.consoleWriteln( "Project directory has not be set.  Please set the directory in the menu via Project -> Set Project Directory." )
            eventPublishService.commandComplete( actionButton )
            return
        }
        else if ( model.isRunning() ) {
            eventPublishService.consoleWriteln( "\ncommand ${model.currentCommand} is already running - can only execute one command at a time, for now at least" )
            return
        }
        else if ( app.models.grailsCommandCenter.consoleShowCommandStartAndEnd ) {
            // TODO: move to ProcessModel ???
            def cmd = "${commandPrefix} ${command}".trim()
            eventPublishService.consoleWriteln( "\n--- running '${cmd}' ---" )
        }

        // update status line
        model.setStatus( model.RUNNING )
        model.currentCommand = command

        // ensure this var is reset to true before every command execution
        writeOutputToConsole = true

        doOutside {

            // timing info
            def startTime = System.currentTimeMillis()

            try {
                def commands = (osCommandPrefix + " " + commandPrefix + " " + command).trim()

                // create process
                ProcessBuilder pb = new ProcessBuilder( commands.split(' ') )
                pb.directory( projectDirectory )
                pb.redirectErrorStream(true)
                model.process = pb.start()

                int pid = processHelper.getPid( model.process )
                model.pid = "(pid ${pid})"

                // capture output from the shell
                // null checks on p in case it gets stopped before this executes
                InputStream is = model.process?.getInputStream()
                // TODO: look at using groovy.ui.SystemOutputInterceptor

                int i=0;
                while ((i=is?.read()) != -1 && writeOutputToConsole ) {
                    //app.event( 'consoleWrite', [((char) i).toString()] )
                    app.controllers.console.consoleWrite( ((char) i).toString() )
                    // bypassing event approach here - we don't need an event on every char
                    // TODO: figure out how to make this better
                }

                // Wait for the process to finish and get the return code
                int exitStatus = model.process?.waitFor() ?: 0;

                if ( exitStatus != 0 )
                    println("${command} exit status: " + exitStatus)

                is?.close();
            }
            catch (IOException e) {
                eventPublishService.consoleWriteln( "\nIOException: ${e.message}" )
                e.printStackTrace()
            }
            catch (InterruptedException e) {
                eventPublishService.consoleWriteln( "\nInterruptedException: ${e.message}" )
                e.printStackTrace()
            }
            catch ( t ) {
                eventPublishService.consoleWriteln( "\nUnexpected Exception: ${t}" )
                t.printStackTrace()
            }
            finally {
                // timing info
                def endTime = System.currentTimeMillis() - startTime

                edt {
                    // model updates
                    if ( model.status != model.STOPPED )
                        model.setStatus( model.COMPLETE )
                    model.currentCommand = ''
                    model.pid = ''
                    model.process = null

                    eventPublishService.commandComplete( actionButton )

                    if ( app.models.grailsCommandCenter.consoleShowCommandStartAndEnd ) {
                        // TODO: move to ProcessModel ???
                        eventPublishService.consoleWriteln( "\n--- command completed in ${endTime}ms (${(endTime/1000 as Double).round(2)}s) (${(endTime/1000/60 as Double).round(2)}m) ---" )
                        // TODO: look at making my output differentiated from normal program output - maybe italics or different font/color/etc
                    }
                }
            }
        }
    }

    /**** end common execute command action ****/

}
