/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ee.raidoseene.releaseplanner.gui.utils;

import ee.raidoseene.releaseplanner.datamodel.Dependencies;
import ee.raidoseene.releaseplanner.datamodel.Dependency;
import ee.raidoseene.releaseplanner.datamodel.Feature;
import ee.raidoseene.releaseplanner.datamodel.Features;
import ee.raidoseene.releaseplanner.datamodel.ReleaseDependency;
import ee.raidoseene.releaseplanner.datamodel.OrderDependency;
import ee.raidoseene.releaseplanner.datamodel.Project;
import java.util.LinkedList;

/**
 *
 * @author Raido Seene
 */
public class FeaturesView {

    // Logic needs to be finished
    private Features features;
    private Dependencies dependencies;
    private final Feature[] featureContainer;

    FeaturesView(Project project) {
        this.features = project.getFeatures();
        this.dependencies = project.getDependencies();
        this.featureContainer = new Feature[features.getFeatureCount()];

        sortFeatures(features);
    }

    public Feature getFeature(int index) {
        return this.featureContainer[index];
    }

    public int getFeatureCount() {
        return this.featureContainer.length;
    }

    private void sortFeatures(Features features) {
        if (this.dependencies.getDependencyCount()
                - this.dependencies.getTypedDependancyCount(ReleaseDependency.class, Dependency.FIXED)
                >= 0) {
            LinkedList list = new LinkedList();
            addDepFeatures(OrderDependency.class, Dependency.SOFTPRECEDENCE, list);
            addDepFeatures(OrderDependency.class, Dependency.HARDPRECEDENCE, list);
            addDepFeatures(OrderDependency.class, Dependency.COUPLING, list);
            addDepFeatures(OrderDependency.class, Dependency.SEPARATION, list);
            addDepFeatures(OrderDependency.class, Dependency.XOR, list);
            addDepFeatures(OrderDependency.class, Dependency.CC, list);
            addDepFeatures(OrderDependency.class, Dependency.CV, list);
            addDepFeatures(OrderDependency.class, Dependency.CU, list);
            addNonDepFeatures(features, list);

            for (int i = 0; i < list.size(); i++) {
                this.featureContainer[i] = (Feature) list.get(i);
            }
        } else {
            for (int i = 0; i < features.getFeatureCount(); i++) {
                this.featureContainer[i] = features.getFeature(i);
            }
        }
    }

    private <T extends OrderDependency> void addDepFeatures(Class<T> cls, Integer criterium, LinkedList list) {
        T[] depList = dependencies.getTypedDependencies(cls, criterium);

        for (int i = 0; i < this.dependencies.getTypedDependancyCount(cls, criterium); i++) {
            if (list.contains(depList[i].getPrimary()) || list.contains(depList[i].getSecondary())) {
                if (list.contains(depList[i].getPrimary()) && list.contains(depList[i].getSecondary())) {
                    //rearrange();
                } else if (list.contains(depList[i].getPrimary())) {
                    list.add(list.indexOf(depList[i].getPrimary()), depList[i].getSecondary());
                } else {
                    list.add(list.indexOf(depList[i].getSecondary()), depList[i].getPrimary());
                }
                    
            } else {
                list.add(depList[i].getPrimary());
                list.add(depList[i].getSecondary());
            }
        }
    }

    private void addNonDepFeatures(Features features, LinkedList list) {
        for (int i = 0; i < features.getFeatureCount(); i++) {
            if (!list.contains(features.getFeature(i))) {
                list.add(features.getFeature(i));
            }
        }
    }
    
    private void rearrange() {
        // TO DO - needs to know existing feature's connections
    }
}
