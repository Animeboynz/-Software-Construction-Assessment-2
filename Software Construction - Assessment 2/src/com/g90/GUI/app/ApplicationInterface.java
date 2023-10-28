package com.g90.gui.app;

//import com.g90.menu.mode.LightDarkMode;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
//import com.g90.menu.mode.ToolBarAccentColor;

/**
 *
 * @author Raven
 */
public class ApplicationInterface extends JPanel {

    private final String[][] optionsMenu = {
        {"~MAIN~"},
        {"Dashboard"},
        {"List Inventory"},
        {"~Product Movements~"},
        {"Manage Inventory", "Add to Inv", "Remove from Inv", "Move Between Inv"},
        {"~Configuration~"},
        {"Create New Inventory"},
        {"Create New Product"},
        {"Logout"}
    };

    public boolean IsOptionsExtended() {
        return OptionsExtended;
    }

    public void OptionMenuExpanded(boolean menuFull) {
        this.OptionsExtended = menuFull;
        if (menuFull) {
            OptionHead.setText(headerName);
            OptionHead.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ? JLabel.LEFT : JLabel.RIGHT);
        } else {
            OptionHead.setText("");
            OptionHead.setHorizontalAlignment(JLabel.CENTER);
        }
        
        Component[] components = panelMenu.getComponents();
        int i = 0;
        
        while (i < components.length) {
            Component component = components[i];
            
            i++;
        }

    }

    private final List<EventApplication> events = new ArrayList<>();
    private boolean OptionsExtended = true;
    private final String headerName = "Inventory Manager";

    protected final boolean hideMenuTitleOnMinimum = true;
    protected final int inset_left_side = 5;
    protected final int vertical_gap_title = 5;
    protected final int Side_menu_width_max_size = 250;
    protected final int side_menu_width_min_size = 60;
    protected final int horizontal_gap_header = 5;

    public ApplicationInterface() {
        initialize();
    }

    private void initialize() {
        setLayout(new MenuLayout());
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:20,2,2,2;"
                + "background:$Menu.background;"
                + "arc:10");
        OptionHead = new JLabel(headerName);
        OptionHead.setIcon(new ImageIcon(getClass().getResource("/com/g90/GUI/assets/logo.png")));
        OptionHead.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$Menu.header.font;"
                + "foreground:$Menu.foreground");

        //  Menu
        OptionsScroll = new JScrollPane();
        panelMenu = new JPanel(new CustomApplicationOptionsLayout(this));
        panelMenu.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5;"
                + "background:$Menu.background");

        OptionsScroll.setViewportView(panelMenu);
        OptionsScroll.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:null");
        JScrollBar verticalScrollBar = OptionsScroll.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(10);
        menuBuilding();

        add(OptionHead);
        add(OptionsScroll);

    }

    private void menuBuilding() {
    int index = 0;
    int i = 0; // Initialization for the while loop

    while (i < optionsMenu.length) {
        String option_name = optionsMenu[i][0];
        if (option_name.startsWith("~") && option_name.endsWith("~")) {
            panelMenu.add(createTitle(option_name));
        } else {
            MenuItem Option_item = new MenuItem(this, optionsMenu[i], index++, events);
            panelMenu.add(Option_item);
        }

        i++; // Increment the counter
    }
}


    private JLabel createTitle(String title) {
        String menuName = title.substring(1, title.length() - 1);
        JLabel lbTitle = new JLabel(menuName);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$Menu.label.font;"
                + "foreground:$Menu.title.foreground");
        return lbTitle;
    }

    public void setSelectedMenu(int index, int subIndex) {
        runEvent(index, subIndex);
    }

    protected void setSelected(int index, int subIndex) {
        int size = panelMenu.getComponentCount();
        for (int i = 0; i < size; i++) {
            Component com = panelMenu.getComponent(i);
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                if (item.getMenuIndex() == index) {
                    item.setSelectedIndex(subIndex);
                } else {
                    item.setSelectedIndex(-1);
                }
            }
        }
    }

    protected void runEvent(int index, int subIndex) {
        ApplicationActions menuAction = new ApplicationActions();
        for (EventApplication event : events) {
            event.SelectedOption(index, subIndex, menuAction);
        }
        if (!menuAction.isCancelled()) {
            setSelected(index, subIndex);
        }
    }

    public void addMenuEvent(EventApplication event) {
        events.add(event);
    }

    public void hideMenuItem() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).hideMenuItem();
            }
        }
        revalidate();
    }

    public boolean isHideMenuTitleOnMinimum() {
        return hideMenuTitleOnMinimum;
    }

    public int getMenuTitleLeftInset() {
        return inset_left_side;
    }

    public int getMenuTitleVgap() {
        return vertical_gap_title;
    }

    public int getMenuMaxWidth() {
        return Side_menu_width_max_size;
    }

    public int getMenuMinWidth() {
        return side_menu_width_min_size;
    }

    private JLabel OptionHead;
    private JScrollPane OptionsScroll;
    private JPanel panelMenu;


    private class MenuLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int gap = UIScale.scale(5);
                int sheaderFullHgap = UIScale.scale(horizontal_gap_header);
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int iconWidth = width;
                int iconHeight = OptionHead.getPreferredSize().height;
                int hgap = OptionsExtended ? sheaderFullHgap : 0;
                int accentColorHeight = 0;


                OptionHead.setBounds(x + hgap, y, iconWidth - (hgap * 2), iconHeight);
                int ldgap = UIScale.scale(10);
                int ldWidth = width - ldgap * 2;
                //int ldHeight = lightDarkMode.getPreferredSize().height;
                int ldHeight = 10;
                int ldx = x + ldgap;
                int ldy = y + height - ldHeight - ldgap  - accentColorHeight;

                int menux = x;
                int menuy = y + iconHeight + gap;
                int menuWidth = width;
                int menuHeight = height - (iconHeight + gap) - (ldHeight + ldgap * 2) - (accentColorHeight);
                OptionsScroll.setBounds(menux, menuy, menuWidth, menuHeight);


            }
        }
    }
}