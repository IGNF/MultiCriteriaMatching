
/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * 
 * @copyright IGN
 * 
 * 
 */package fr.ign.cogit.distance.semantique;

import java.io.File;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import fr.ign.cogit.distance.Distance;
import fr.ign.cogit.ontology.OntologieOWL;
import fr.ign.cogit.ontology.similarite.MesureSimilariteSemantique;
import fr.ign.cogit.ontology.similarite.WuPalmerSemanticSimilarity;

/**
 * 
 * @author M-D Van Damme
 */
public class DistanceWuPalmer extends DistanceAbstractSemantique implements Distance {
    
    /** Ontologie. */
    private OntologieOWL onto = null;
    
    /** Default URI ontologie. */
    //private static final String URI_ONTO = "./data/ontology/FusionTopoCartoExtract.owl";
     private static final String URI_ONTO = "./data/ontology/GeOnto.owl";
    
    
    public DistanceWuPalmer(String uri) {
        try {
            File file = new File(uri);
            onto = new OntologieOWL("Onto", file.getPath());
        } catch(Exception e) {
            e.printStackTrace();
        } 
    }
    
    public DistanceWuPalmer() {
    	this(URI_ONTO);
    }
    
    public void close() {
        if (onto != null) {
            onto.close();
        }
    }
    
    public DistanceWuPalmer(OntologieOWL onto) {
        this.onto = onto;
    }
    
    
    @Override
    public double getDistance() {
        float d = (float)(1 - mesureSimilariteWuPalmer(this.attrNameSemRef, this.attrNameSemComp));
        //System.out.println("Distance WP " + this.attrNameSemRef.toLowerCase() + "-" + this.attrNameSemComp.toLowerCase() + " = " + d);
        return d;
    }
    
    public static String getClass(String attrNameSemRef) {
        String type = "";
        try {
            File file = new File(URI_ONTO);
            OntologieOWL onto = new OntologieOWL("Onto", file.getPath());
            
            RDFResource rS = onto.getOWLModel().getRDFResource(attrNameSemRef.toLowerCase());
            if (rS != null) {
                type = rS.getName();
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        } 
        return type;
    }
    
    public double mesureSimilariteWuPalmer(String attrNameSemRef, String typeComp) {
        
        RDFResource rS = onto.getOWLModel().getRDFResource(attrNameSemRef.toLowerCase());
        RDFResource rT = onto.getOWLModel().getRDFResource(typeComp.toLowerCase());
        double scoreSimilariteSemantique;
        if (rS != null && rT != null ){
            MesureSimilariteSemantique mesureSim = new WuPalmerSemanticSimilarity(onto);
            scoreSimilariteSemantique = mesureSim.calcule(rS, rT);
            //System.out.println(attrNameSemRef.toLowerCase() + ", " + typeComp.toLowerCase() + " = " + scoreSimilariteSemantique);
            // LOGGER.trace("Score similarité sémantique = " + scoreSimilariteSemantique);
        }
        else scoreSimilariteSemantique = Double.NaN;
        return scoreSimilariteSemantique;
    }

    
    @Override
    public String getNom() {
        return "WuPalmer";
    }
}
