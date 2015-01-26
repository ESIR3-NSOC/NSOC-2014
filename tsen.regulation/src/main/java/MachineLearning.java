import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;


public class MachineLearning{
	
	public static void main(String[] args) throws Exception{
		//read datas on the file .arff
		BufferedReader breader = null;
		breader = new BufferedReader(new FileReader("////monfichier.arff"));
		//create a new instance of datas to close the BufferedReader
		Instances train = new Instances(breader);
		train.setClassIndex(train.numAttributes() -1);
		breader.close();
		//create a linearRegression model using data
		LinearRegression lr = new LinearRegression();
		lr.buildClassifier(train);
		
		Evaluation eval = new Evaluation(train);
		eval.crossValidateModel(nB, train, 10, new Random(1));
				
	}
}
