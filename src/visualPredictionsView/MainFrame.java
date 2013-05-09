package visualPredictionsView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import weka.classifiers.Classifier;
import weka.core.Instances;

import algorithms.Algorithms;
import algorithms.ObjectReader;
import algorithms.ObjectWriter;

import Prediction.ClassifierPrediction;
import constants.Constants;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements MouseListener {
	final int TOP_X = 20;
	private static final String[] SUFIXES = new String[] {"1"}; // "RandomForest",
																// "J48",
																// "NaiveBayes",
																// "JRip",
																// "SMO"
	private JPanel mainPanel;
	private JComboBox<String> musicClassifiersCB;
	private JComboBox<String> speechClassifiersCB;
	private JComboBox<String> fileNamesCB;
	private JComboBox<String> testFileNamesCB;

	private JLabel musicLabel;
	private JLabel speechLabel;
	private JLabel fileNamesLabel;
	private JLabel testFileNamesLabel;
	private JLabel numMusicLabel;
	private JLabel numSpeechLabel;

	private JTextField numMusicField;
	private JTextField numSpeechField;
	private JButton applyButton;
	private JButton applyWithNeighbourButton;
	private JButton applyTestButton;

	private Map<String, Vector<Boolean>> trueClassClassification_Music;
	private Map<String, Vector<Boolean>> trueClassClassification_Speech;

	private Vector<ClassifierPrediction> predictionsClassifier_Music;
	private Vector<ClassifierPrediction> predictionsClassifier_Speech;

	public MainFrame() {
		initPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(mainPanel);
		setLocation(0, 0);
		initLabel();
		initComboBox();
		initTextFiled();
		initButton();
		readAllPredictionData();
		readTrueData();
		initComboxData();

		setSize(800, 270);
		setResizable(false);
		setVisible(true);
	}

	private void initComboxData() {
		for (int i = 0; i < predictionsClassifier_Music.size(); i++) {
			ClassifierPrediction current = predictionsClassifier_Music.get(i);
			musicClassifiersCB.addItem(i + "." + current.getClassifier().getClass().toString());
		}

		for (int i = 0; i < predictionsClassifier_Speech.size(); i++) {
			ClassifierPrediction current = predictionsClassifier_Speech.get(i);
			speechClassifiersCB.addItem(i + "." + current.getClassifier().getClass().toString());
		}
	}

	private void initLabel() {
		numMusicLabel = new JLabel("Num of Neighbours");
		numMusicLabel.setLocation(TOP_X, 20);
		numMusicLabel.setSize(100, 20);
		mainPanel.add(numMusicLabel);

		numSpeechLabel = new JLabel("Num of Neighbours");
		numSpeechLabel.setLocation(TOP_X, 90);
		numSpeechLabel.setSize(100, 20);
		mainPanel.add(numSpeechLabel);

		musicLabel = new JLabel("Music");
		musicLabel.setLocation(TOP_X + 120, 20);
		musicLabel.setSize(50, 20);
		mainPanel.add(musicLabel);

		speechLabel = new JLabel("Speech");
		speechLabel.setLocation(TOP_X + 120, 90);
		speechLabel.setSize(50, 20);
		mainPanel.add(speechLabel);

		fileNamesLabel = new JLabel("Files");
		fileNamesLabel.setLocation(TOP_X + 450, 20);
		fileNamesLabel.setSize(50, 20);
		mainPanel.add(fileNamesLabel);

		testFileNamesLabel = new JLabel("Test Files");
		testFileNamesLabel.setLocation(TOP_X + 450, 90);
		testFileNamesLabel.setSize(50, 20);
		mainPanel.add(testFileNamesLabel);
	}

	private void initComboBox() {
		musicClassifiersCB = new JComboBox<String>();
		musicClassifiersCB.setLocation(TOP_X + 120, 40);
		musicClassifiersCB.setSize(250, 25);
		mainPanel.add(musicClassifiersCB);

		speechClassifiersCB = new JComboBox<String>();
		speechClassifiersCB.setLocation(TOP_X + 120, 110);
		speechClassifiersCB.setSize(250, 25);
		mainPanel.add(speechClassifiersCB);

		fileNamesCB = new JComboBox<String>(Constants.FILE_NAMES);
		fileNamesCB.setLocation(TOP_X + 450, 40);
		fileNamesCB.setSize(250, 25);
		mainPanel.add(fileNamesCB);

		testFileNamesCB = new JComboBox<String>(Constants.TEST_FILE_NAMES);
		testFileNamesCB.setLocation(TOP_X + 450, 110);
		testFileNamesCB.setSize(250, 25);
		mainPanel.add(testFileNamesCB);
	}

	private void initTextFiled() {
		numMusicField = new JTextField();
		numMusicField.setLocation(TOP_X, 40);
		numMusicField.setSize(90, 25);
		mainPanel.add(numMusicField);

		numSpeechField = new JTextField();
		numSpeechField.setLocation(TOP_X, 110);
		numSpeechField.setSize(90, 25);
		mainPanel.add(numSpeechField);
	}

	private void initButton() {
		applyButton = new JButton("Apply");
		applyButton.setLocation(TOP_X + 300, 165);
		applyButton.setSize(100, 40);
		applyButton.addMouseListener(this);
		mainPanel.add(applyButton);

		applyWithNeighbourButton = new JButton("Apply Neighbour Algorthim");
		applyWithNeighbourButton.setLocation(TOP_X + 410, 165);
		applyWithNeighbourButton.setSize(160, 40);
		applyWithNeighbourButton.addMouseListener(this);
		mainPanel.add(applyWithNeighbourButton);

		applyTestButton = new JButton("Apply Test");
		applyTestButton.setLocation(TOP_X + 580, 165);
		applyTestButton.setSize(140, 40);
		applyTestButton.addMouseListener(this);
		mainPanel.add(applyTestButton);
	}

	private void initPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
	}

	@SuppressWarnings("unchecked")
	private void readPredictionDataFor(String s) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream("MusicPredictions" + s + ".obj");
			in = new ObjectInputStream(fis);
			if (predictionsClassifier_Music == null)
				predictionsClassifier_Music = (Vector<ClassifierPrediction>) in.readObject();
			else
				predictionsClassifier_Music.addAll((Vector<ClassifierPrediction>) in.readObject());
			fis.close();
			in.close();

			fis = new FileInputStream("SpeechPredictions" + s + ".obj");
			in = new ObjectInputStream(fis);
			if (predictionsClassifier_Speech == null)
				predictionsClassifier_Speech = (Vector<ClassifierPrediction>) in.readObject();
			else
				predictionsClassifier_Speech.addAll((Vector<ClassifierPrediction>) in.readObject());
			fis.close();
			in.close();
		} catch (Exception e) {

		}
	}

	@SuppressWarnings("unchecked")
	private void readTrueData() {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream("MusicTrueClassificationMap.obj");
			in = new ObjectInputStream(fis);
			trueClassClassification_Music = (Map<String, Vector<Boolean>>) in.readObject();
			fis.close();
			in.close();

			fis = new FileInputStream("SpeechTrueClassificationMap.obj");
			in = new ObjectInputStream(fis);
			trueClassClassification_Speech = (Map<String, Vector<Boolean>>) in.readObject();
			fis.close();
			in.close();
		} catch (Exception e) {

		}
	}

	private void readAllPredictionData() {
		readBestClassifiers();
		for (String s : SUFIXES) {
			System.out.println("\n################### CLASSIFIER " + s + " IS LOADING #################");
			readPredictionDataFor(s);
		}
	}

	private void readBestClassifiers() {
		if (predictionsClassifier_Music == null)
			predictionsClassifier_Music = new Vector<ClassifierPrediction>();
		if (predictionsClassifier_Speech == null)
			predictionsClassifier_Speech = new Vector<ClassifierPrediction>();
		ClassifierPrediction musicPrediction = ObjectReader.loadObject("BestMusicPrediction.obj");
		ClassifierPrediction speechPrediction = ObjectReader.loadObject("BestSpeechPrediction.obj");
		if (musicPrediction == null) {
			//Classifier musicClassifier = ObjectReader.loadObject("MusicBestClassifier.obj");
			Classifier musicClassifier = ObjectReader.loadObject("NewMusicBestClassifier.obj");
			System.out.println("\n################ MUSIC BEST CLASSIFIER IS GENERATING #################");
			if (musicClassifier != null) {
				musicPrediction = new ClassifierPrediction(musicClassifier);
				for (String fileName : Constants.FILE_NAMES) {
					Instances musicFile = Algorithms.getDataFromFile(Constants.MUSIC_FOLDER + fileName);
					musicPrediction.addPrediction(fileName, Algorithms.predict(musicClassifier, musicFile));
				}
				System.out.println("\n################### MUSIC BEST CLASSIFIER GENERATED #################");
				predictionsClassifier_Music.add(musicPrediction);
				ObjectWriter.writeObject(musicPrediction, "BestMusicPrediction.obj");
				ObjectWriter.writeObject(predictionsClassifier_Music, "MusicPredictionsBest.obj");
			}
		} else {
			System.out.println("\n################### MUSIC BEST CLASSIFIER LOADED #################");
			predictionsClassifier_Music.add(musicPrediction);
		}

		if (speechPrediction == null) {
			//Classifier speechClassifier = ObjectReader.loadObject("SpeechBestClassifier.obj");
			Classifier speechClassifier = ObjectReader.loadObject("NewBestSpeechClassifier.obj");
			System.out.println("\n################ SPEECH BEST CLASSIFIER IS GENERATING #################");
			if (speechClassifier != null) {
				speechPrediction = new ClassifierPrediction(speechClassifier);
				for (String fileName : Constants.FILE_NAMES) {
					Instances speechFile = Algorithms.getDataFromFile(Constants.SPEECH_FOLDER + fileName);
					speechPrediction.addPrediction(fileName, Algorithms.predict(speechClassifier, speechFile));
				}
				System.out.println("\n################### SPEECH BEST CLASSIFIER GENERATED #################");
				predictionsClassifier_Speech.add(speechPrediction);
				ObjectWriter.writeObject(speechPrediction, "BestSpeechPrediction.obj");
				ObjectWriter.writeObject(predictionsClassifier_Speech, "SpeechPredictionsBest.obj");
			}
		} else {
			System.out.println("\n################### SPEECH BEST CLASSIFIER LOADED #################");
			predictionsClassifier_Speech.add(speechPrediction);
		}
	}

	private void plotValues() {
		String selectedFileName = (String) fileNamesCB.getSelectedItem();
		plotValues(predictionsClassifier_Music.get(musicClassifiersCB.getSelectedIndex()).getPrediction(selectedFileName),
				predictionsClassifier_Speech.get(speechClassifiersCB.getSelectedIndex()).getPrediction(selectedFileName));
	}

	private void plotValues(Vector<Boolean> newPredClassifier_Music, Vector<Boolean> newPredClassifier_Speech) {
		String selectedFileName = (String) fileNamesCB.getSelectedItem();
		PlotFrame pf = new PlotFrame(trueClassClassification_Music.get(selectedFileName), trueClassClassification_Speech.get(selectedFileName),
				newPredClassifier_Music, newPredClassifier_Speech, (String) fileNamesCB.getSelectedItem(), (String) musicClassifiersCB.getSelectedItem()
						+ " # " + (String) speechClassifiersCB.getSelectedItem());
		new Thread(pf).start();
	}
	
	private void plotValues(Vector<Boolean> musicPrediction, Vector<Boolean> speechPrediction, Vector<Boolean> newPredClassifier_Music,
			Vector<Boolean> newPredClassifier_Speech, int musicNumber, int speechNumber) {
		PlotFrame pf = new PlotFrame(musicPrediction, speechPrediction,
				newPredClassifier_Music, newPredClassifier_Speech, (String) testFileNamesCB.getSelectedItem(), (String) musicClassifiersCB.getSelectedItem()
						+ " # " + (String) speechClassifiersCB.getSelectedItem(), "Prediction Class Classification Chart", "Music " + musicNumber + " And Speech " + speechNumber + " Neighboring Chart");
		new Thread(pf).start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource().equals(applyButton))
			plotValues();
		else if (e.getSource().equals(applyTestButton)) {
			String selectedFileName = (String) testFileNamesCB.getSelectedItem();
			Instances musicFile = Algorithms.getDataFromFile(Constants.TEST_MUSIC_FOLDER + selectedFileName);
			Vector<Boolean> musicPrediction = Algorithms.predict(predictionsClassifier_Music.get(musicClassifiersCB.getSelectedIndex()).getClassifier(),
					musicFile);
			Instances speechFile = Algorithms.getDataFromFile(Constants.TEST_SPEECH_FOLDER + selectedFileName);
			Vector<Boolean> speechPrediction = Algorithms.predict(predictionsClassifier_Speech.get(speechClassifiersCB.getSelectedIndex()).getClassifier(),
					speechFile);
			int musicNumber = 0, speechNumber = 0;
			try {
				musicNumber = Integer.parseInt(numMusicField.getText());
			} catch (NumberFormatException ex) {}
			try {
				speechNumber = Integer.parseInt(numSpeechField.getText());
			} catch (NumberFormatException ex) {}
			Vector<Boolean> newPredClassifier_Music = Algorithms.neighborNegotiiation(musicPrediction, musicNumber);
			Vector<Boolean> newPredClassifier_Speech = Algorithms.neighborNegotiiation(speechPrediction, speechNumber);
			plotValues(musicPrediction, speechPrediction, newPredClassifier_Music, newPredClassifier_Speech, musicNumber, speechNumber);
		} else {
			String selectedFileName = (String) fileNamesCB.getSelectedItem();
			int musicNumber = 0, speechNumber = 0;
			try {
				musicNumber = Integer.parseInt(numMusicField.getText());
			} catch (NumberFormatException ex) {}
			try {
				speechNumber = Integer.parseInt(numSpeechField.getText());
			} catch (NumberFormatException ex) {}
			Vector<Boolean> newPredClassifier_Music = Algorithms.neighborNegotiiation(predictionsClassifier_Music.get(musicClassifiersCB.getSelectedIndex())
					.getPrediction(selectedFileName), musicNumber);
			Vector<Boolean> newPredClassifier_Speech = Algorithms.neighborNegotiiation(predictionsClassifier_Speech.get(speechClassifiersCB.getSelectedIndex())
					.getPrediction(selectedFileName), speechNumber);
			plotValues(newPredClassifier_Music, newPredClassifier_Speech);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
