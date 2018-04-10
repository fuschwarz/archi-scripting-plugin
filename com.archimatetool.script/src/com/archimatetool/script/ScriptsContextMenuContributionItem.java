/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;

import com.archimatetool.editor.views.tree.ITreeModelView;


/**
 * Contribute Menu Items to context menu
 * 
 * @author Phillip Beauvoir
 */
public class ScriptsContextMenuContributionItem extends ContributionItem implements IWorkbenchContribution {
    
    private MenuManager menuManager;
    
    private ISelectionService selectionService;

    public ScriptsContextMenuContributionItem() {
    }

    public ScriptsContextMenuContributionItem(String id) {
        super(id);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
    
    @Override
    public void fill(Menu menu, int index) {
        if(menuManager != null) {
            menuManager.dispose();
        }
        
        menuManager = new MenuManager();
        
        fillItems(menuManager, ArchiScriptPlugin.INSTANCE.getUserScriptsFolder().listFiles());
        
        for(IContributionItem item : menuManager.getItems()) {
            item.fill(menu, index++);
        }
    }

    private void fillItems(MenuManager menuManager, File[] files) {
        for(File file : files) {
            if(file.isDirectory()) {
                MenuManager subMenu = new MenuManager(file.getName());
                menuManager.add(subMenu);
                fillItems(subMenu, file.listFiles());
            }
            else {
                menuManager.add(new Action(file.getName()) {
                    @Override
                    public void run() {
                        System.out.println("Running " + file.getName() + " on selection: " + getCurrentSelection());
                    }
                });
            }
        }
    }
    
    private ISelection getCurrentSelection() {
        return selectionService.getSelection(ITreeModelView.ID);
    }

    public void initialize(IServiceLocator serviceLocator) {
        this.selectionService = serviceLocator.getService(ISelectionService.class);;
    }
}
