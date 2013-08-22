package grailscommandcenter

import java.awt.event.KeyEvent


actions {
    // file menu actions
    action(
        id: 'menuOpenAction',
        name: 'Open Project',
        //mnemonic: 'O',
        accelerator: shortcut('O'),
        closure: controller.openProject )
    action(
        id: 'menuCloseAction',
        name: 'Close Project',
        //mnemonic: 'C',
        closure: controller.closeProject )
    action(
        id: 'menuSaveAction',
        name: 'Save Project Config',
        //mnemonic: 'S',
        accelerator: shortcut('S'),
        closure: controller.saveProject )
    action(
        id: 'menuSaveAsAction',
        name: 'Save Project Config As...',
        closure: controller.saveProjectAs )
    action(
        id: 'menuSelectProjectDirectoryAction',
        name: 'Set Project Directory',
        closure: controller.selectProjectDirectory )
    action(
        id: 'menuQuitAction',
        name: 'Exit',
        //mnemonic: 'X',
        //accelerator: shortcut('Q'),
        closure: controller.quit )
    // edit menu options
    action(
        id: 'menuEditCommandHistoryAction',
        name: 'Edit Command History',
        //mnemonic: 'E',
        //accelerator: shortcut('E'),
        closure: controller.editCommandHistory )
    action(
        id: 'menuClearCommandHistoryAction',
        name: 'Clear Command History',
        closure: controller.clearCommandHistory )
    action(
        id: 'menuFindInConsoleAction',
        name: 'Find in Console',
        //mnemonic: 'F',
        accelerator: shortcut('F'),
        closure: controller.findInConsole )
    action(
        id: 'menuClearConsoleAction',
        name: 'Clear Console',
        accelerator: shortcut('W'),
        closure: controller.clearConsole )
    action(
        id: 'menuCopyConsoleAction',
        name: 'Copy Console',
        //mnemonic: 'C',
        accelerator: shortcut('C'),
        closure: controller.copyConsole )

    // TODO: add action to save console output to file with shortcut ctrl+S
    // note: save current contents of text area in view, not necessarily what the underlying model representation is.  that way if the user modifies the output they can save off just the data they want.

    action(
        id: 'menuStopCommandAction',
        name: 'Stop Current Process',
        accelerator: shortcut('Q'),
        closure: controller.stop )
    // preferences menu options
    action(
        id: 'menuPrefsProjectAction',
        name: 'Project Options',
        closure: controller.prefsProject )
    action(
        id: 'menuPrefsDisplayAction',
        name: 'Display Options',
        //mnemonic: 'D',
        //accelerator: shortcut('D'),
        closure: controller.prefsDisplay )
    action(
        id: 'menuIncreaseFontSizeAction',
        name: 'Increase Font Size',
        //mnemonic: '+',
        accelerator: shortcut(KeyEvent.VK_EQUALS),
        closure: controller.increaseFontSize )
    action(
        id: 'menuDecreaseFontSizeAction',
        name: 'Decrease Font Size',
        //mnemonic: '-',
        accelerator: shortcut(KeyEvent.VK_MINUS),
        closure: controller.decreaseFontSize )
    // help menu options
    action(
        id: 'menuHelpTutorialAction',
        name: 'Tutorial',
        closure: controller.helpTutorial )
    action(
        id: 'menuHelpAboutAction',
        name: 'About',
        closure: controller.helpAbout )
    // end menu actions
}