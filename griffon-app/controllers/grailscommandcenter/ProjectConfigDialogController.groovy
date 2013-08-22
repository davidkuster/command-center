package grailscommandcenter

import griffon.transform.Threading
import java.awt.Window


class ProjectConfigDialogController {

    def model
    def view
    def builder
    
    protected dialog

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    def show = { Window window = null ->
        window = window ?: Window.windows.find{it.focused}
        if(!dialog || dialog.owner != window) {
            dialog = builder.dialog(
                owner: window,
                title: "Project Options", //model.title,
                resizable: true, //model.resizable,
                modal: true ) { //model.modal) {
                container(view.content)        
            }
            /*if(model.width > 0 && model.height > 0) {
                dialog.preferredSize = [model.width, model.height]
            }*/
            dialog.pack()
        }
        int x = window.x + (window.width - dialog.width) / 2
        int y = window.y + (window.height - dialog.height) / 2
        dialog.setLocation(x, y)
        dialog.visible = true
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    def hide = { evt = null ->
        dialog?.visible = false
        dialog?.dispose()
        dialog = null
    }

}
