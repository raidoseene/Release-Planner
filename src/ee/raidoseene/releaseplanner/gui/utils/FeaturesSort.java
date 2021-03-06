/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ee.raidoseene.releaseplanner.gui.utils;

import ee.raidoseene.releaseplanner.datamodel.Dependencies;
import ee.raidoseene.releaseplanner.datamodel.Feature;
import ee.raidoseene.releaseplanner.datamodel.Features;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Raido Seene
 */
public class FeaturesSort {

    private LinkedList<FeatureNode> featureList;
    private final Map<Feature, FeatureNode> featureNodeLinking;
    
    private Features feats;

    public FeaturesSort(Features features) {
        featureList = new LinkedList();
        featureNodeLinking = new HashMap<>();
        int featuresNo = features.getFeatureCount();

        for (int i = 0; i < featuresNo; i++) {
            Feature f = features.getFeature(i);
            FeatureNode fNode = new FeatureNode(f);
            featureList.add(fNode);
            featureNodeLinking.put(f, fNode);
        }
        
        feats = features;
    }

    /*
    public void rePopulate(Features features) {
        int featuresNo = features.getFeatureCount();

        for (int i = 0; i < featuresNo; i++) {
            Feature f = features.getFeature(i);
            if (featureList.get(i).getFeature() != f) {
                FeatureNode fNode = new FeatureNode(f);
                featureList.remove(i);
                featureList.add(i, fNode);
                featureNodeLinking.put(f, fNode);
            }
        }
    }
    */

    public Feature[] sort(Dependencies dependencies) {
        int distance;
        int newDistance;
        boolean gotBetter = true;
        ArrayList<Feature> newFeaturesList = new ArrayList<>();
        LinkedList<FeatureNode> backupList = (LinkedList<FeatureNode>)featureList.clone();
        
        /*
        for(FeatureNode fNode: featureList) {
            backupList.add(fNode);
        }
        */
        
        calculateDistances(dependencies);
        distance = cumulativeDistance();
        
        while(gotBetter) {
            
            gotBetter = false;
            for(int i = featureList.size() - 1; i >= 0; i--) {
                for(int j = 0; j < i; j++) {
                    FeatureNode fNode = backupList.get(i);
                    featureList.remove(i);
                    
                    featureList.add(j, fNode);
                    
                    calculateDistances(dependencies);
                    newDistance = cumulativeDistance();
                    if(newDistance < distance) {
                        distance = newDistance;
                        backupList = (LinkedList<FeatureNode>)featureList.clone();
                        gotBetter = gotBetter | true;
                    } else {
                        featureList = (LinkedList<FeatureNode>)backupList.clone();
                    }
                }
                for(int j = i + 1; j < featureList.size(); j++) {
                    FeatureNode fNode = backupList.get(i);
                    featureList.remove(i);
                    
                    featureList.add(j, fNode);
                    
                    calculateDistances(dependencies);
                    newDistance = cumulativeDistance();
                    if(newDistance < distance) {
                        distance = newDistance;
                        backupList = (LinkedList<FeatureNode>)featureList.clone();
                        gotBetter = gotBetter | true;
                    } else {
                        featureList = (LinkedList<FeatureNode>)backupList.clone();
                    }
                }
            }
        }
        
        System.out.println("\\nSorted list:");
        for(int i = 0; i < featureList.size(); i++) {
            FeatureNode fNode = featureList.get(i);
            newFeaturesList.add(fNode.getFeature());
            System.out.println(feats.getFeatureIndex(fNode.getFeature()));
        }
        System.out.println("End of sorted list");
        
        return newFeaturesList.toArray(new Feature[featureList.size()]);
    }

    private void calculateDistances(Dependencies dependencies) {
        for (int i = 0; i < featureList.size(); i++) {
            FeatureNode fNode = featureList.get(i);
            Feature feature = fNode.getFeature();
            Feature[] dependantF = dependencies.getDependantFeatures(feature);

            fNode.resetDistances();
            
            for (int f = 0; f < dependantF.length; f++) {
                int index = i - featureList.indexOf(featureNodeLinking.get(dependantF[f]));
                if(index < 0) {
                    fNode.addDownDistance(Math.abs(index));
                } else {
                    fNode.addUpDistance(index);
                }
            }
        }
    }
    
    private int cumulativeDistance() {
        int sum = 0;
        for(int i = 0; i < featureList.size(); i++) {
            FeatureNode fNode = featureList.get(i);
            sum += fNode.getDownDistance();
            sum += fNode.getUpDistance();
        }
        return sum;
    }
}
