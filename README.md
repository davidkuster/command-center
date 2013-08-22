# command-center

Mini-IDE for Grails development to replace Command Line/Terminal


Welcome to the Grails Command Center utility.  The intent behind this tool is to make Grails development more productive and enjoyable by putting a layer on top of directly interacting with the command line.  This is especially true for those confined to Windows machines and its 1980s-era DOS prompt, but is presumably applicable for anyone who has tired of typing 'grails run-app' for what feels like the 10,000th time.  Note that IDE users will probably not find this helpful, but for those using the fantastic Sublime Text (or something similar) this will hopefully be a worthwhile companion.


## Tutorial

This tool comes preloaded with a button configuration for some commonly used Grails commands.  The command buttons can be edited or deleted by right-clicking on them, or a new button can be added via the '*' button.  Commands can also be typed directly into the command combo box and executed by pressing enter.

Note that the combo box will remember any manually entered commands, which can be accessed using the drop-down aspect of the combo box.  The history of these commands can be cleared using the Edit menu.  An option to edit the history (to remove mistyped commands, for instance) is forthcoming.

Console output can be searched or cleared/wiped via buttons or keystrokes (Ctrl/Cmd-F and Ctrl/Cmd-W, respectively).  In addition, the currently running process can be killed via the 'Stop' button or via a keystoke (Ctrl/Cmd-Q).

Console output can be edited directly in the app to help focus in on key info.  However, note that currently updates to the text area are not reflected back to the internal data representation, so any subsequent output will ignore changes.  However, the updated text can be copied to the system clipboard by highlighting it and using the Copy button or Ctrl/Cmd-C.

Before starting, set the Grails project directory using the menu (Project -> Set Project Directory).  On exit, the tool will automatically write your current config to a hidden file in your home directory (.griffon-command-center), but you can also save and load other config files using the menu.

Additional options can be set using the Preferences -> Project Options menu.  These are:

### Command Prefix

The default setting is to assume all commands start with "grails".  However, this can be modified - for instance, I use a script I've named "g.sh" to automatically determine the correct version of Grails to load based on the current project's application.properties file.  If you'd prefer to have more flexibility in the tool this value can be removed and the buttons can be updated from "run-app" to "grails run-app".  Additionally, you can specify the full path to the "grails" script as an alternative way to run multiple Grails versions on the same system.

(The above mentioned script can be found at: http://www.componentix.com/blog/16)


### Console Max Chars

In order to not have the app memory grow continuously, the output is limited to a specified number of characters.  Set this value to 0 to turn off limiting.


### Smart Console Output

Grails 2.0 updated its default output to minimize the number of lines written, so "| Compiling 2 source files" and "| Compiling 2 source files..." will be shown on the same line.  This setting is an attempt to mimic that behavior, but can be turned off if desired.


### Show Current Command Info in Console

This setting controls whether additional output is written to the console to indicate the current command being run, as well as completion and timing info of that command.  Currently it looks like this:

    --- running 'grails stats' ---
    (normal command output)
    --- command completed in 4108ms (4.11s) (0.07m) ---



### Show Welcome Message at Startup

Pretty self-explanatory.  :)




## About

This tool is still very much a work in progress but I hope you find it useful.  The app is written in Griffon (v1.0.0) so it should be fairly straightforward for Grails developers to understand and modify.


### Bug Fixes / Future direction / TODOs

- recognize when the console is asking for user input.  currently output is only written to the text area at newlines, so if you're being prompted to upgrade a plugin (`Do you want to upgrade plugin XYZ to version 1.0? (y/n):`) this won't be reflected in the UI and the app will appear to hang.  this is a SERIOUS PROBLEM so be aware of it if you try using this tool.

- potentially let each button accept multiple commands to be executed, so clean/compile/run (for example) could be done with a single click.

- implement a tabbed interface so multiple projects can be open at once, or multiple commands can be run against the same project simultaneously.  (for instance, keeping run-app going while running unit tests in a different tab.)

- potentially let buttons accept input parameters.  thus, buttons like 'create-domain-class com.package.%1' become possible, and other uses for this tool become more feasible.  (such as a wrapper around git commands.)

- add option to pause automatic scrolling of console output.

- use something smarter than a text area for the console window (ie. JTextPane/JEditorPane/some kind of code editor/etc) to allow for highlighting of output, additional font styling, etc.  additionally allow the user to configure keywords/phrases to be highlighted and colors for each.

- potentially integrate with Grails' interactive mode so the JVM is only spun up once and kept running in between commands for faster execution of subsequent commands.

- user-configurable shortcuts for the command buttons.

- extend Jide SearchableBar/Searchable to indicate how many instances of searched text were found, at least when using highlight all checkbox.