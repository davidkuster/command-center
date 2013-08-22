package grailscommandcenter


actions {

    // stop currently executing process
    action(
        id: 'stopAction',
        name: 'Stop',
        closure: controller.stop )

    // create new user button
    action(
        id: 'createNewButtonAction',
        name: '*',
        closure: controller.createNewButton )

    // command comboBox - direct entry of command
    action(
        id: 'manualCommandAction',
        name: 'Manual',
        closure: controller.manualCommand )
    
}