/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ee.raidoseene.releaseplanner.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Raido
 */
public class Group extends NamedObject {
    private final List<Feature> featureContainer;

    Group() {
         this.featureContainer = new ArrayList<>();
    }

    protected void addFeature(Feature f) {
        this.featureContainer.add(f);
        
    }

    public void removeFeature(Feature f) {
        this.featureContainer.remove(f);
    }
    
    public Feature getFeature(int index) {
        return this.featureContainer.get(index);
    }

    public int getFeatureCount() {
        return featureContainer.size();
    }

    boolean contains(Feature f) {
        return this.featureContainer.contains(f);
    }
    
}