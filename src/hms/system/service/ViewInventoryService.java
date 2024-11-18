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

    private void handleRequestItemOption() {
        boolean request = Prompt.getBooleanInput("Do you want to request for more for this item? (y/n)");
        int requested_amount = Prompt.getIntInput("Enter the amount you want to request for: ");

        ctx.getManager(InventoryManager.class)
            .updateInventoryItemRequestStatus(selected.getMedicalName(), request, requested_amount);

        // Update display
        menuNav.getCurrentMenu().title = getItemInfoDisplay(selected);
    }

    private void handleUpdateStockOption() {
        int newStock = Prompt.getIntInput("Enter the change in stock amount: ");
        ctx.getManager(InventoryManager.class)
            .updateInventoryItemStock(selected.getMedicalName(), newStock);

        int remainingStock = Math.max(0, newStock - selected.getRequestedAmount());

        ctx.getManager(InventoryManager.class)
            .updateInventoryItemRequestStatus(selected.getMedicalName(), remainingStock == 0, remainingStock);

        // Update display
        menuNav.getCurrentMenu().title = getItemInfoDisplay(selected);
    }

    private void handleResolveRequestOption() {
        ctx.getManager(InventoryManager.class)
            .updateInventoryItemRequestStatus(selected.getMedicalName(), false, 0);

        // Update display
        menuNav.getCurrentMenu().title = getItemInfoDisplay(selected);
    }

    private void handleChangeLowStockAlertOption() {
        boolean alert = Prompt.getBooleanInput("Do you want to change the low stock alert for this item? (y/n)");
        if (!alert) return;

        int alert_amount = Prompt.getIntInput("Enter the new low stock alert amount: ");

        ctx.getManager(InventoryManager.class)
            .updateInventoryItemLowStockThreshold(selected.getMedicalName(), alert_amount);

        // Update display
        menuNav.getCurrentMenu().title = getItemInfoDisplay(selected);
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

    private void onItemSelect(InventoryItem item){
        selected = item;

        SimpleMenu menu = new SimpleMenu(getItemInfoDisplay(item), null);

        if (hasRequestItemOption)
            menu.addOption(new UserOption("Request item", this::handleRequestItemOption));

        if (hasUpdateStockOption)
            menu.addOption(new UserOption("Update stock", this::handleUpdateStockOption));

        if (hasResolveRequestOption)
            menu.addOption(new UserOption("Resolve request", this::handleResolveRequestOption));

        if (hasChangeLowStockAlertOption)
            menu.addOption(new UserOption("Change low stock alert", this::handleChangeLowStockAlertOption));

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
