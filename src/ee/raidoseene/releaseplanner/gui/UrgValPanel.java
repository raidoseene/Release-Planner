/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee.raidoseene.releaseplanner.gui;

import ee.raidoseene.releaseplanner.backend.ProjectManager;
import ee.raidoseene.releaseplanner.datamodel.Feature;
import ee.raidoseene.releaseplanner.datamodel.Project;
import ee.raidoseene.releaseplanner.datamodel.Stakeholder;
import ee.raidoseene.releaseplanner.datamodel.Stakeholders;
import ee.raidoseene.releaseplanner.gui.utils.ContentListLayout;
import ee.raidoseene.releaseplanner.gui.utils.ContentPanel;
import ee.raidoseene.releaseplanner.gui.utils.ScrollablePanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author risto
 */
public final class UrgValPanel extends JPanel {

    public static final String TITLE_STRING = "Feature urgency & value";

    public UrgValPanel() {
        this.setLayout(new GridLayout(1, 1));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        JTabbedPane tabs = new JTabbedPane();
        this.add(tabs);

        UVTab<Stakeholder> spanel = new UVTab<>();
        tabs.addTab("By stakeholder", spanel);
        this.addHierarchyListener(spanel);

        UVTab<Feature> fpanel = new UVTab<>();
        tabs.addTab("By feature", fpanel);
        this.addHierarchyListener(fpanel);
    }

    private final class UVTab<T> extends JPanel implements HierarchyListener {

        private final JComboBox featc, stakec;
        private final ScrollablePanel scrollable;
        private final String all = "All";

        private UVTab() {
            this.setLayout(new BorderLayout(10, 10));
            this.setBorder(new EmptyBorder(10, 10, 10, 10));

            Container ct, c = new Container();
            c.setLayout(new BorderLayout(10, 10));
            this.add(BorderLayout.PAGE_START, c);
            String[] sel = new String[]{all};

            ct = new Container();
            ct.setLayout(new GridLayout(1, 2, 10, 10));
            c.add(BorderLayout.LINE_START, ct);
            this.featc = new JComboBox(sel);
            ct.add(this.featc);
            ct.add(new JLabel("Select feature group"));

            ct = new Container();
            ct.setLayout(new GridLayout(1, 2, 10, 10));
            c.add(BorderLayout.LINE_END, ct);
            this.stakec = new JComboBox(sel);
            ct.add(this.stakec);
            ct.add(new JLabel("Select stakeholder"));

            this.scrollable = new ScrollablePanel();
            this.add(BorderLayout.CENTER, new JScrollPane(this.scrollable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
            this.scrollable.setLayout(new ContentListLayout(ContentPanel.class));
            this.scrollable.setBorder(new EmptyBorder(10, 10, 10, 10));
        }
        
        private void listFilteredContent() {
            try {
                
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        @Override
        public void hierarchyChanged(HierarchyEvent he) {
            int mask = HierarchyEvent.SHOWING_CHANGED;
            if ((he.getChangeFlags() & mask) == mask && he.getChanged().isShowing()) {
                Project project = ProjectManager.getCurrentProject();
                boolean changed = false;
                
                try {
                    Object item = this.featc.getSelectedItem();
                    // TODO:
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                
                try {
                    Object item = this.stakec.getSelectedItem();
                    Stakeholders stakeholders = project.getStakeholders();
                    int count = stakeholders.getStakeholderCount();
                    this.stakec.removeAllItems();
                    this.stakec.addItem(this.all);
                    this.stakec.setSelectedIndex(0);
                    for (int i = 0; i < count; i++) {
                        this.stakec.addItem(stakeholders.getStakeholder(i).getName());
                    }
                    
                    this.stakec.setSelectedItem(item);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                
                if (changed) {
                    this.listFilteredContent();
                }
            }
        }

    }

}
