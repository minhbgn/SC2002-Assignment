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

    public boolean hasRequestItemOption = false;
    public boolean hasUpdateStockOption = false;
    public boolean hasResolveRequestOption = false;
    public boolean hasChangeLowStockAlertOption = false;

    private InventoryItem selected;

    public ViewInventoryService(ManagerContext ctx){
        this.ctx = ctx;
    }

    private String getItemInfoDisplay(InventoryItem item){
        String itemInfo = "Item details:\n\n";
        itemInfo += "Name: " + item.getMedicalName() + "\n";
        itemInfo += "In stock: " + item.getStock();
        itemInfo += item.isLowStock() ? " (Low stock!)\n" : "\n";
        
        if (item.isRequested())
            itemInfo += "Requested: Yes (" + item.getRequestedAmount() + " requested)";
        else itemInfo += "Requested: No";

        return itemInfo;
    }

    private void handleRequestItemOption() {
        boolean request = Prompt.getBooleanInput("Do you want to request for more for this item? (y/n)");
        int requested_amount = Prompt.getIntInput("Enter the amount you want to request for: ");

        ctx.getManager(InventoryManager.class)
            .updateInventoryItemRequestStatus(selected.getMedicalName(), request, requested_amount);

        menuNav.popMenu();
    }

    private void onItemSelect(InventoryItem item){
        selected = item;
        
        SimpleMenu menu = new SimpleMenu(getItemInfoDisplay(item), null);

        if (hasRequestItemOption)
            menu.addOption(new UserOption("Request item", this::handleRequestItemOption));

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
