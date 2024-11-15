package hms.system.service;

import hms.inventory.InventoryItem;
import hms.manager.InventoryManager;
import hms.manager.ManagerContext;
import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.PaginatedListSelector;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;

public class ViewInventoryService implements IService {
    private final ManagerContext ctx;

    private MenuNavigator menuNav;

    public ViewInventoryService(ManagerContext ctx){
        this.ctx = ctx;
    }

    private void onItemSelect(InventoryItem item){
        String itemInfo = "Item details:\n\n";
        itemInfo += "Name: " + item.getMedicalName() + "\n";
        itemInfo += "In stock: " + item.getStock();
        itemInfo += item.isLowStock() ? " (Low stock!)\n" : "\n";
        
        if (item.isRequested())
            itemInfo += "Requested: Yes (" + item.getRequestedAmount() + " requested)";
        else itemInfo += "Requested: No";

        SimpleMenu menu = new SimpleMenu(itemInfo, null);

        menu.addOption(new UserOption("Request item", () -> {
            boolean request = Prompt.getBooleanInput("Do you want to request for more for this item? (y/n)");
            int requested_amount = Prompt.getIntInput("Enter the amount you want to request for: ");

            ctx.getManager(InventoryManager.class)
                .updateInventoryItemRequestStatus(item.getMedicalName(), request, requested_amount);

            menuNav.popMenu();
        }));

        menuNav.addMenu(menu);
    }

    private AbstractMenu getMenu(){
        InventoryItem[] items = ctx.getManager(InventoryManager.class)
            .getInventoryItem(null)
            .toArray(InventoryItem[]::new);

        PaginatedListSelector<InventoryItem> viewer =
            new PaginatedListSelector<>("Inventory", items, this::onItemSelect);

        return viewer;
    }

    @Override
    public void execute(MenuNavigator menuNav) {
        this.menuNav = menuNav;

        menuNav.addMenu(getMenu());
    }
}
