package grailscommandcenter


// create actions
build(ActionsMenuBar)



// TODO: look at sublime text for ideas & placement of options
menuBar = menuBar { 
    menu( 'File' ) {
        menuItem menuQuitAction
    }
    menu( 'Edit' ) { 
        menuItem 'Configure Button Actions', enabled:false
        separator()
        menuItem menuEditCommandHistoryAction, enabled:false
        menuItem menuClearCommandHistoryAction
    }
    menu( 'Console' ) {
        menuItem menuClearConsoleAction
        menuItem menuCopyConsoleAction
        menuItem menuFindInConsoleAction
        separator()
        menuItem menuStopCommandAction
    }
    menu( 'Project' ) {
        menuItem menuOpenAction
        menuItem 'Recent Projects >', enabled:false
        separator()
        menuItem menuSaveAction, enabled:false
        menuItem menuSaveAsAction
        menuItem menuCloseAction, enabled:false
        separator()
        menuItem menuSelectProjectDirectoryAction
    }
    menu( 'Preferences' ) {
        menuItem menuPrefsProjectAction
        menuItem menuPrefsDisplayAction, enabled:false
        separator()
        menuItem menuIncreaseFontSizeAction
        menuItem menuDecreaseFontSizeAction
    }
    menu( 'Help' ) {
        menuItem menuHelpTutorialAction
        menuItem menuHelpAboutAction
    }
}