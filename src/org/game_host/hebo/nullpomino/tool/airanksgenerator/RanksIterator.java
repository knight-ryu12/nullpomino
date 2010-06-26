package org.game_host.hebo.nullpomino.tool.airanksgenerator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class RanksIterator extends ProgressMonitor implements PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	private Ranks ranks;
	private Ranks ranksFrom;
	private String outputFile;
	private JFrame parent;
	private int numIterations;
	private int iteration;
	private int size;
	private float lastError;
	private int lastErrorMax;
	 private SwingWorker<Void, String> mySwingWorker;
	
    class MySwingWorker extends SwingWorker<Void, String> {
    
    private int totalParts;

    private RanksIteratorPart [] ranksIteratorPart;
   
        public MySwingWorker( int totalParts) {
        	
        	this.totalParts=totalParts;
        
            setProgress(0);
            
           
        }
        @Override
       
        public Void doInBackground() {
        	
        	 ranksIteratorPart=new RanksIteratorPart[totalParts];
        	 
        	 for (int n=0;n<numIterations;n++){
        		 iteration=n;
        	   for (int i=0;i<totalParts;i++){
        	   ranksIteratorPart[i]=new RanksIteratorPart(this,ranks,i,totalParts);	;
        	   ranksIteratorPart[i].start();
        	   }
        	for (int i=0;i<totalParts;i++){
        		try {
					ranksIteratorPart[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	lastError=ranks.getErrorPercentage();
        	lastErrorMax=ranks.getMaxError();
        	if (n!=numIterations-1){
        	  ranksFrom=ranks.getRanksFrom();
        	  ranksFrom.setRanksFrom(ranks);
        	  ranks=ranksFrom;
        	}
        	
        	}
        	
            return null;
        }
        
        public void iterate(){
        	
        	
        	
        	
        	if (ranks.completionPercentageIncrease()){
        	
        	  this.setProgress(ranks.getCompletionPercentage());
        	}
        }
 

        @Override
        protected void done() {
            try {
                
                FileOutputStream fos=null;
                ObjectOutputStream out=null;
                fos=new FileOutputStream(outputFile);
                out = new ObjectOutputStream(fos);
                ranks.freeRanksFrom();
                out.writeObject(ranks);
                out.close();
               
            } catch(Exception e) {
                e.printStackTrace();
            }
            setProgress(100);
        	ranksFrom=null;
        	ranks=null;
            
       
        	//new RanksResult(parent,ranks,100,false);
         
        }
        public void cancel(){
        	for (int i=0;i<totalParts;i++){
        		 ranksIteratorPart[i].interrupt();
        	}
        }
    }
    
public RanksIterator(JFrame parent,String inputFile,String outputFile, int numIterations){
	
	super(parent,"Computing ranks...","",0,100);
	this.outputFile=outputFile;
	
	this.numIterations=numIterations;
	FileInputStream fis = null;
	ObjectInputStream in = null;
	if (inputFile.trim().isEmpty())
		ranksFrom=new Ranks(4,9);
	else {
		  try {
			fis = new FileInputStream(inputFile);
			   in = new ObjectInputStream(fis);
			   ranksFrom = (Ranks)in.readObject();
			   in.close();
			   
		} catch (FileNotFoundException e) {
			ranksFrom=new Ranks(4,9);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	ranks=new Ranks(ranksFrom);
	
	size=ranks.getSize();
	
	mySwingWorker =this.new MySwingWorker(4);
	mySwingWorker.addPropertyChangeListener(this);
	mySwingWorker.execute();
	
	
	
}
public void propertyChange(PropertyChangeEvent evt) {
	 if ("progress" == evt.getPropertyName() ) {
           int progress = (Integer) evt.getNewValue();
           setProgress(progress);
     
           
           String message =
               String.format("It.:%d(%d%%) - Error : %d/%d", iteration,ranks.getCompletionPercentage(),ranks.getMaxError(),lastErrorMax);
          setNote(message);
	 } 
           
   if (isCanceled()) {
                   this.mySwingWorker.cancel(true);
                   
               
              
   }
       

	
}

}
