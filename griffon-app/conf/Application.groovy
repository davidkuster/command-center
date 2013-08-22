application {
    title = 'GrailsCommandCenter'
    startupGroups = ['grailsCommandCenter']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "projectConfigDialog"
    'projectConfigDialog' {
        model      = 'grailscommandcenter.ProjectConfigDialogModel'
        view       = 'grailscommandcenter.ProjectConfigDialogView'
        controller = 'grailscommandcenter.ProjectConfigDialogController'
    }

    // MVC Group for "process"
    'process' {
        model      = 'grailscommandcenter.ProcessModel'
        controller = 'grailscommandcenter.ProcessController'
        view       = 'grailscommandcenter.ProcessView'
    }

    // MVC Group for "commandButton"
    'commandButton' {
        model      = 'grailscommandcenter.CommandButtonModel'
        controller = 'grailscommandcenter.CommandButtonController'
        view       = 'grailscommandcenter.CommandButtonView'
    }

    // MVC Group for "consolePanel"
    'consolePanel' {
        model      = 'grailscommandcenter.ConsolePanelModel'
        controller = 'grailscommandcenter.ConsolePanelController'
        view       = 'grailscommandcenter.ConsolePanelView'
    }

    // MVC Group for "grailsCommandCenter"
    'grailsCommandCenter' {
        model      = 'grailscommandcenter.GrailsCommandCenterModel'
        controller = 'grailscommandcenter.GrailsCommandCenterController'
        view       = 'grailscommandcenter.GrailsCommandCenterView'
    }

}
