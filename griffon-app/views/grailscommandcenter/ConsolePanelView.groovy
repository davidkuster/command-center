package grailscommandcenter

import com.jidesoft.swing.*


// any way to reference other view script with this defined?
hSpacer = { rigidArea( width:2 ) }


build(ActionsConsole)


consoleTextArea = textArea( text:bind { model.output }, lineWrap:true, tabSize:4, font:new Font(Font.MONOSPACED, Font.PLAIN, 12) )
// TODO: figure out an elegant way to tie the font size to the saved config


consoleButtons = hbox() {
    button( id:'clearConsoleButton', clearConsoleAction, toolTipText:'Clear Console' )
    hSpacer()
    button( copyConsoleAction, toolTipText:'Copy Console Text' )
    hSpacer()
    button( findInConsoleAction, toolTipText:'Find in Console' )
}


searchable = SearchableUtils.installSearchable( consoleTextArea )

searchBar = SearchableBar.install( searchable, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK),
new SearchableBar.Installer() {
	public void openSearchBar(SearchableBar sb) {
		sb.setVisible( true )
	}
	public void closeSearchBar(SearchableBar sb) {
		sb.setVisible( false )
	}
});

noparent {
	//searchBar.setCompact( true )
	searchBar.setVisible( false )

	consoleTextArea.setLineWrap( true )
	consoleTextArea.setWrapStyleWord( true )
}


return consoleTextArea
