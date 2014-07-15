 package com.vishnu.sensortest; 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationSequencesReader;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationVectorReader;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;
  
public class TestGesture {
	OpdfMultiGaussianFactory initFactoryPunch=null;
	Reader learnReaderPunch=null;
	List<List<ObservationVector>> learnSequencesPunch=null;
	KMeansLearner<ObservationVector> kMeansLearnerPunch=null;
	Hmm<ObservationVector> initHmmPunch=null;
	Hmm<ObservationVector> learntHmmPunch=null;
	Hmm<ObservationVector> learntHmmScrolldown=null;
	Hmm<ObservationVector> learntHmmSend=null;
	 String root = Environment.getExternalStorageDirectory().toString();
	    File myDir = new File(root + "/Data");    
	   
public void	train() {
	 myDir.mkdirs();
	// Create HMM for punch gesture
	Boolean exception =false;
	int x=10;
	while(!exception){
	try{
    OpdfMultiGaussianFactory initFactoryPunch = new OpdfMultiGaussianFactory(
            3);
    
    Reader learnReaderPunch = new FileReader(new File (myDir, "punchlearn.seq"));
    List<List<ObservationVector>> learnSequencesPunch = ObservationSequencesReader
            .readSequences(new ObservationVectorReader(), learnReaderPunch);
    learnReaderPunch.close();

    KMeansLearner<ObservationVector> kMeansLearnerPunch = new KMeansLearner<ObservationVector>(
            x, initFactoryPunch, learnSequencesPunch);
    // Create an estimation of the HMM (initHmm) using one iteration of the
    // k-Means algorithm
    Hmm<ObservationVector> initHmmPunch = kMeansLearnerPunch.iterate();
    // Use BaumWelchLearner to create the HMM (learntHmm) from initHmm
    
    BaumWelchLearner baumWelchLearnerPunch = new BaumWelchLearner();
     this.learntHmmPunch = baumWelchLearnerPunch.learn(
            initHmmPunch, learnSequencesPunch);
    exception=true;
    System.out.println(x);
	  }
	  catch(Exception e){
		  x--;
		  //System.out.println(x);
		  
	  }

}
    // Create HMM for scroll-down gesture
	Boolean exception1 =false;
	int x1=10;
	while(!exception1){
	try{
    OpdfMultiGaussianFactory initFactoryScrolldown = new OpdfMultiGaussianFactory(
            3);

    Reader learnReaderScrolldown = new FileReader(new File (myDir, "ltortrain.seq"));
    List<List<ObservationVector>> learnSequencesScrolldown = ObservationSequencesReader
            .readSequences(new ObservationVectorReader(),
                    learnReaderScrolldown);
    learnReaderScrolldown.close();

    KMeansLearner<ObservationVector> kMeansLearnerScrolldown = new KMeansLearner<ObservationVector>(
            x1, initFactoryScrolldown, learnSequencesScrolldown);
    // Create an estimation of the HMM (initHmm) using one iteration of the
    // k-Means algorithm
    Hmm<ObservationVector> initHmmScrolldown = kMeansLearnerScrolldown
            .iterate();

    // Use BaumWelchLearner to create the HMM (learntHmm) from initHmm
    BaumWelchLearner baumWelchLearnerScrolldown = new BaumWelchLearner();
     this.learntHmmScrolldown = baumWelchLearnerScrolldown
            .learn(initHmmScrolldown, learnSequencesScrolldown);
     exception1=true;
     //System.out.println("here1");
     System.out.println(x1);
	  }
	  catch(Exception e){
		  x1--;
		  //System.out.println(x1);
		  
	  }
	}
    // Create HMM for send gesture
	Boolean exception2 =false;
	int x2=10;
	while(!exception2){
	try{
    OpdfMultiGaussianFactory initFactorySend = new OpdfMultiGaussianFactory(
            3);

    Reader learnReaderSend = new FileReader(new File (myDir, "rtoltrain.seq"));
    List<List<ObservationVector>> learnSequencesSend = ObservationSequencesReader
            .readSequences(new ObservationVectorReader(), learnReaderSend);
    learnReaderSend.close();

    KMeansLearner<ObservationVector> kMeansLearnerSend = new KMeansLearner<ObservationVector>(
            x2, initFactorySend, learnSequencesSend);
    // Create an estimation of the HMM (initHmm) using one iteration of the
    // k-Means algorithm
    Hmm<ObservationVector> initHmmSend = kMeansLearnerSend.iterate();

    // Use BaumWelchLearner to create the HMM (learntHmm) from initHmm
    BaumWelchLearner baumWelchLearnerSend = new BaumWelchLearner();
     this.learntHmmSend = baumWelchLearnerSend.learn(
            initHmmSend, learnSequencesSend);
     exception2=true;
     System.out.println(x2);
	  }
	  catch(Exception e){
		  x2--;
		  //System.out.println(x2);
		  
	  }
	}
	}
	
    public String test(File seqfilename) throws Exception{
        Reader testReader = new FileReader(seqfilename);
        List<List<ObservationVector>> testSequences = ObservationSequencesReader
                .readSequences(new ObservationVectorReader(), testReader);
        testReader.close();
        
        short gesture; // punch = 1, scrolldown = 2, send = 3
        double punchProbability, scrolldownProbability, sendProbability;
        for (int i = 0; i < testSequences.size(); i++) {
       
            punchProbability = this.learntHmmPunch.probability(testSequences
                    .get(i));
            //System.out.println(this.learntHmmPunch.probability(testSequences.get(i)));
            gesture = 1;
            //System.out.println(this.learntHmmScrolldown);
            scrolldownProbability = this.learntHmmScrolldown.probability(testSequences
                    .get(i));
            
            if (scrolldownProbability > punchProbability) {
                gesture = 2;
            }
            sendProbability = this.learntHmmSend.probability(testSequences
                    .get(i));
            //System.out.println(punchProbability +","+scrolldownProbability +","+sendProbability);
            if ((gesture == 1 && sendProbability > punchProbability)
                    || (gesture == 2 && sendProbability > scrolldownProbability)) {
                gesture = 3;
            }
            Log.i("probabilities", punchProbability + "   " + sendProbability  + "   " + scrolldownProbability);
            
            if (gesture == 1) {
            	System.out.println("This is a punch gesture");
            	return "punch";
            } else if (gesture == 2) {
                System.out.println("This is a right-left gesture");
            	return "right";
            } else if (gesture == 3) {
            	System.out.println("This is a left to right gesture");
            	return "left";
            }else{
            	return "others";
            }
        }
		return "others";
    }
  
} 