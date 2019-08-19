package cz.mg.resampler.gui.utilities;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.UIManager;


/**
 *  Class is used to help with some custom menu problems.
 */
public class MenuUtilities {
    /**
     *  This method makes the window listen, match and execute accelerators for given menu.
     *  Custom menus does not listen for accelerators globally (or at least not when hidden).
     *  This method adds global keyboard listener that checks for an accelerator match.
     *  If it finds a match for some menu item and the given window is active, then
     *  the item's action is executed and the event is consumed.
     *  @param window: the window which is checked to be active
     *  @param menu: the menu whose item's accelerators are being checked
     */
    public static void addMenuAcceleratorListener(final Window window, final JPopupMenu menu){
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(!window.isActive()) return false;
                for(Component component : menu.getComponents()){
                    if(component instanceof JMenuItem){
                        JMenuItem item = (JMenuItem) component;
                        if(item.getAccelerator() == null) continue;
                        if(item.getAccelerator().equals(KeyStroke.getKeyStrokeForEvent(e))){
                            item.doClick();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }
    
    /**
     *  Makes menu to consume mouse click when its hidden.
     *  When menu is hidden by a mouse click, the click event is not consumed by default.
     *  Calling this method fixes the issue.
     */
    public static void makeMenuConsumeClickEvent(){
        UIManager.put("PopupMenu.consumeEventOnClose", Boolean.TRUE);
    }
}
