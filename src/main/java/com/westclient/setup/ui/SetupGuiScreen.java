package com.westclient.setup.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class SetupGuiScreen extends GuiScreen {

    private static final int PANEL_BG = 0xFF15181F;
    private static final int PANEL_BORDER = 0xFF1D2636;
    private static final int TEXT_PRIMARY = 0xFFE5ECFF;
    private static final int TEXT_MUTED = 0xFF888888;
    private static final int TEXT_BASE = 0xFFFFFFFF;
    private static final int TEXT_ACTIVE = 0xFF6FB7FF;

    private final List<CategoryEntry> categories = new ArrayList<>();
    private int selectedCategory = 0;
    private String selectedSubCategory = "";
    private float openProgress = 0f;
    private int scrollOffset = 0;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        openProgress = 0f;
        if (categories.isEmpty()) {
            initCategories();
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    private void initCategories() {
        categories.add(new CategoryEntry("About"));
        categories.add(new CategoryEntry("GUI"));
        categories.add(new CategoryEntry("General"));
        categories.add(new CategoryEntry("Miscellaneous"));
        categories.add(new CategoryEntry("Garden"));
        categories.add(new CategoryEntry("Fishing"));
        categories.add(new CategoryEntry("Foraging"));
        categories.add(new CategoryEntry("Mining"));
        categories.add(new CategoryEntry("Hunting"));
        categories.add(new CategoryEntry("Combat"));
        categories.add(new CategoryEntry("Slayer", Arrays.asList("Enderman", "Blaze", "Vampire")));
        categories.add(new CategoryEntry("Crimson Isle"));
        categories.add(new CategoryEntry("The Rift"));
        categories.add(new CategoryEntry("Kuudra"));
        categories.add(new CategoryEntry("Dungeons"));
        categories.add(new CategoryEntry("Events", Arrays.asList("Diana")));
        categories.add(new CategoryEntry("Inventory", Arrays.asList("Bazaar", "Auction House")));
        categories.add(new CategoryEntry("Chat"));
        categories.add(new CategoryEntry("Dev"));
        categories.add(new CategoryEntry("Notifications"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        openProgress = Math.min(1f, openProgress + 0.04f);
        drawCategoryPanel(mouseX, mouseY);
        drawInfoPanel();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCategoryPanel(int mouseX, int mouseY) {
        int categoryWidth = 260;
        int infoWidth = 360;
        int gap = 30;
        int totalWidth = categoryWidth + gap + infoWidth;
        int baseX = (width - totalWidth) / 2;

        int panelWidth = categoryWidth;
        int fullHeight = height - 140;
        int panelHeight = Math.max(40, (int) (fullHeight * Math.max(0.05f, openProgress)));
        int panelX = baseX;
        int panelY = (height - panelHeight) / 2;

        drawPanel(panelX, panelY, panelWidth, panelHeight);
        FontRenderer fr = fontRendererObj;
        String header = "Categories";
        fr.drawString(header, panelX + (panelWidth - fr.getStringWidth(header)) / 2, panelY + 15, TEXT_MUTED);

        // Calculate content area
        int contentTop = panelY + 37;
        int contentBottom = panelY + panelHeight - 10;
        int contentHeight = contentBottom - contentTop;
        
        // Calculate total content height
        int rowHeight = 20;
        int totalContentHeight = 0;
        for (CategoryEntry entry : categories) {
            totalContentHeight += rowHeight;
            if (entry.expanded && entry.hasChildren()) {
                totalContentHeight += entry.children.size() * rowHeight;
            }
        }
        
        // Update scroll offset bounds
        int maxScroll = Math.max(0, totalContentHeight - contentHeight);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
        
        // Draw scrollbar with black outline
        int trackX = panelX + 10;
        int trackTop = contentTop;
        int trackBottom = contentBottom;
        
        if (totalContentHeight > contentHeight) {
            // Draw black outline around track
            Gui.drawRect(trackX - 1, trackTop - 1, trackX + 4, trackTop, 0xFF000000);
            Gui.drawRect(trackX - 1, trackBottom, trackX + 4, trackBottom + 1, 0xFF000000);
            Gui.drawRect(trackX - 1, trackTop, trackX, trackBottom, 0xFF000000);
            Gui.drawRect(trackX + 3, trackTop, trackX + 4, trackBottom, 0xFF000000);
            
            // Draw scrollbar track
            Gui.drawRect(trackX, trackTop, trackX + 3, trackBottom, PANEL_BORDER);
            
            float scrollRatio = (float) scrollOffset / maxScroll;
            int thumbHeight = Math.max(10, (int) (contentHeight * (contentHeight / (float) totalContentHeight)));
            int thumbTop = trackTop + (int) ((trackBottom - trackTop - thumbHeight) * scrollRatio);
            
            // Draw black outline around thumb
            Gui.drawRect(trackX - 1, thumbTop - 1, trackX + 4, thumbTop, 0xFF000000);
            Gui.drawRect(trackX - 1, thumbTop + thumbHeight, trackX + 4, thumbTop + thumbHeight + 1, 0xFF000000);
            Gui.drawRect(trackX - 1, thumbTop, trackX, thumbTop + thumbHeight, 0xFF000000);
            Gui.drawRect(trackX + 3, thumbTop, trackX + 4, thumbTop + thumbHeight, 0xFF000000);
            
            // Draw thumb
            Gui.drawRect(trackX, thumbTop, trackX + 3, thumbTop + thumbHeight, 0xFF3B4A63);
        } else {
            // Draw black outline around default thumb
            Gui.drawRect(trackX - 1, trackTop - 1, trackX + 4, trackTop, 0xFF000000);
            Gui.drawRect(trackX - 1, trackTop + 20, trackX + 4, trackTop + 21, 0xFF000000);
            Gui.drawRect(trackX - 1, trackTop, trackX, trackTop + 20, 0xFF000000);
            Gui.drawRect(trackX + 3, trackTop, trackX + 4, trackTop + 20, 0xFF000000);
            
            Gui.drawRect(trackX, trackTop, trackX + 3, trackTop + 20, 0xFF3B4A63);
        }

        // Draw visible categories
        int startY = contentTop - scrollOffset;
        int rowY = startY;
        
        for (int i = 0; i < categories.size(); i++) {
            CategoryEntry entry = categories.get(i);
            boolean selected = selectedSubCategory.isEmpty() && selectedCategory == i;
            
            // Check if this row is visible
            if (rowY + rowHeight >= contentTop && rowY <= contentBottom) {
                int textWidth = fr.getStringWidth(entry.name);
                int arrowX = panelX + 20;
                if (entry.hasChildren()) {
                    if (entry.expanded) {
                        drawArrowDown(arrowX, rowY + 4, TEXT_MUTED);
                    } else {
                        drawArrowRight(arrowX, rowY + 4, TEXT_MUTED);
                    }
                }
                drawCenteredText(entry.name, panelX + 15, panelWidth - 25, rowY, selected, TEXT_BASE);
            }
            rowY += rowHeight;
            
            if (entry.expanded && entry.hasChildren()) {
                for (String child : entry.children) {
                    if (rowY + rowHeight >= contentTop && rowY <= contentBottom) {
                        boolean subSelected = selectedCategory == i && child.equals(selectedSubCategory);
                        drawCenteredText(child, panelX + 15, panelWidth - 25, rowY, subSelected, TEXT_MUTED);
                    }
                    rowY += rowHeight;
                }
            }
        }
    }

    private void drawInfoPanel() {
        int categoryWidth = 260;
        int infoWidth = 360;
        int gap = 30;
        int totalWidth = categoryWidth + gap + infoWidth;
        int baseX = (width - totalWidth) / 2;

        int panelWidth = infoWidth;
        int fullHeight = height - 140;
        int panelHeight = Math.max(40, (int) (fullHeight * Math.max(0.05f, openProgress)));
        int panelX = baseX + categoryWidth + gap;
        int panelY = (height - panelHeight) / 2;

        drawPanel(panelX, panelY, panelWidth, panelHeight);
        fontRendererObj.drawString("Information", panelX + 10, panelY + 15, TEXT_MUTED);
        String activeCategory = selectedSubCategory.isEmpty() ? categories.get(selectedCategory).name : selectedSubCategory;
        fontRendererObj.drawString("Selected Category", panelX + 10, panelY + 47, TEXT_BASE);
        fontRendererObj.drawString(activeCategory, panelX + 10, panelY + 63, TEXT_ACTIVE);
        fontRendererObj.drawString("Status", panelX + 10, panelY + 93, TEXT_BASE);
        fontRendererObj.drawSplitString(
            "No configuration here yet. WestClient will stream your modules into this panel once you build them.",
            panelX + 10,
            panelY + 109,
            panelWidth - 20,
            TEXT_MUTED
        );
    }

    private void drawPanel(int x, int y, int w, int h) {
        Gui.drawRect(x - 1, y - 1, x + w + 1, y + h + 1, 0x22000000);
        Gui.drawRect(x, y, x + w, y + h, PANEL_BG);
        Gui.drawRect(x, y, x + w, y + 1, PANEL_BORDER);
        Gui.drawRect(x, y + h - 1, x + w, y + h, PANEL_BORDER);
        Gui.drawRect(x, y, x + 1, y + h, PANEL_BORDER);
        Gui.drawRect(x + w - 1, y, x + w, y + h, PANEL_BORDER);
        
        // Draw inside border
        int borderPadding = 10;
        Gui.drawRect(x + borderPadding, y + borderPadding, x + w - borderPadding, y + borderPadding + 1, PANEL_BORDER);
        Gui.drawRect(x + borderPadding, y + h - borderPadding - 1, x + w - borderPadding, y + h - borderPadding, PANEL_BORDER);
        Gui.drawRect(x + borderPadding, y + borderPadding, x + borderPadding + 1, y + h - borderPadding, PANEL_BORDER);
        Gui.drawRect(x + w - borderPadding - 1, y + borderPadding, x + w - borderPadding, y + h - borderPadding, PANEL_BORDER);
    }

    private void drawCenteredText(String text, int startX, int availableWidth, int y, boolean active, int baseColor) {
        int textWidth = fontRendererObj.getStringWidth(text);
        int x = startX + (availableWidth - textWidth) / 2;
        int color = active ? TEXT_ACTIVE : baseColor;
        fontRendererObj.drawString(text, x, y, color);
        if (active) {
            Gui.drawRect(x, y + 11, x + textWidth, y + 12, TEXT_ACTIVE);
        }
    }

    private void drawArrowDown(int centerX, int y, int color) {
        for (int i = 0; i < 4; i++) {
            Gui.drawRect(centerX - 3 + i, y + i, centerX + 3 - i, y + i + 1, color);
        }
    }

    private void drawArrowRight(int centerX, int y, int color) {
        Gui.drawRect(centerX - 4, y - 1, centerX - 2, y + 4, color);
        for (int i = 0; i < 4; i++) {
            Gui.drawRect(centerX - 1 + i, y + i, centerX + 1 + i, y + i + 1, color);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        handleCategoryClick(mouseX, mouseY);
    }

    private boolean handleCategoryClick(int mouseX, int mouseY) {
        int categoryWidth = 260;
        int infoWidth = 360;
        int gap = 30;
        int totalWidth = categoryWidth + gap + infoWidth;
        int baseX = (width - totalWidth) / 2;
        int panelWidth = categoryWidth;
        int panelHeight = Math.max(40, (int) ((height - 140) * Math.max(0.05f, openProgress)));
        int panelX = baseX;
        int panelY = (height - panelHeight) / 2;

        if (mouseX < panelX || mouseX > panelX + panelWidth || mouseY < panelY || mouseY > panelY + panelHeight) {
            return false;
        }

        int contentTop = panelY + 37;
        int contentBottom = panelY + panelHeight - 10;
        int rowHeight = 20;
        int startY = contentTop - scrollOffset;
        int rowY = startY;
        
        for (int i = 0; i < categories.size(); i++) {
            CategoryEntry entry = categories.get(i);
            
            // Check if clicking on main category
            if (mouseY >= rowY && mouseY <= rowY + rowHeight) {
                if (entry.hasChildren()) {
                    // If it has children, toggle the dropdown
                    entry.expanded = !entry.expanded;
                } else {
                    // If no children, select it
                    selectedCategory = i;
                    selectedSubCategory = "";
                }
                return true;
            }
            rowY += rowHeight;
            
            // Check if clicking on subcategory
            if (entry.expanded && entry.hasChildren()) {
                for (String sub : entry.children) {
                    if (mouseY >= rowY && mouseY <= rowY + rowHeight) {
                        selectedCategory = i;
                        selectedSubCategory = sub;
                        return true;
                    }
                    rowY += rowHeight;
                }
            }
        }
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = org.lwjgl.input.Mouse.getEventDWheel();
        if (wheel != 0) {
            int categoryWidth = 260;
            int infoWidth = 360;
            int gap = 30;
            int totalWidth = categoryWidth + gap + infoWidth;
            int baseX = (width - totalWidth) / 2;
            int panelWidth = categoryWidth;
            int panelHeight = Math.max(40, (int) ((height - 140) * Math.max(0.05f, openProgress)));
            int panelX = baseX;
            int panelY = (height - panelHeight) / 2;
            
            int mouseX = org.lwjgl.input.Mouse.getEventX() * width / mc.displayWidth;
            int mouseY = height - org.lwjgl.input.Mouse.getEventY() * height / mc.displayHeight - 1;
            
            // Only scroll if mouse is over category panel
            if (mouseX >= panelX && mouseX <= panelX + panelWidth && 
                mouseY >= panelY && mouseY <= panelY + panelHeight) {
                scrollOffset -= wheel / 120 * 20;
                int contentTop = panelY + 37;
                int contentBottom = panelY + panelHeight - 10;
                int contentHeight = contentBottom - contentTop;
                
                int rowHeight = 20;
                int totalContentHeight = 0;
                for (CategoryEntry entry : categories) {
                    totalContentHeight += rowHeight;
                    if (entry.expanded && entry.hasChildren()) {
                        totalContentHeight += entry.children.size() * rowHeight;
                    }
                }
                
                int maxScroll = Math.max(0, totalContentHeight - contentHeight);
                scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private static class CategoryEntry {
        private final String name;
        private final List<String> children;
        private boolean expanded;

        private CategoryEntry(String name) {
            this(name, null);
        }

        private CategoryEntry(String name, List<String> children) {
            this.name = name;
            this.children = children == null ? new ArrayList<>() : new ArrayList<>(children);
            this.expanded = false;
        }

        private boolean hasChildren() {
            return !children.isEmpty();
        }
    }
}



