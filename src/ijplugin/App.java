/*
 * Copyright (C) 2017 stefko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ijplugin;

import algorithm_tester.AlgorithmTester;
import algorithm_tester.EvaluationAlgorithm;
import algorithm_tester.FeedbackController;
import algorithm_tester.ImageGenerator;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.process.ImageProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stefko
 */
public class App extends AlgorithmTester {
    private ImagePlus imp;
    private Worker worker;
    
    public App() {
        super();
        generator.getNextImage();
        generator.getNextImage();
        imp = new ImagePlus("Sim window", generator.getStack());
        imp.show();
    }
    
    public void startSimulating() {
        worker = new Worker(this, generator, controller, analyzers, imp);
        worker.start();
    }
    
    public void stopSimulating() {
        worker.stop = true;
        try {
            worker.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setSetpoint(double value) {
        controller.setTarget(value);
    }
    
    public void analyzerSetupDialog() {
        GenericDialog gd = new GenericDialog("Analyzer setup");
        for (String key: analyzers.keySet()) {
            gd.addMessage(String.format("%s:",key));
            LinkedHashMap<String,Integer> settings = analyzers.get(key).getCustomParameters();
            for (String s_key: settings.keySet()) {
                gd.addNumericField(s_key, settings.get(s_key), 0);
            }
        }
        gd.showDialog();
        
        if (gd.wasCanceled())
            return;
        
        for (String key: analyzers.keySet()) {
            LinkedHashMap<String,Integer> settings = new LinkedHashMap<String,Integer>();
            for (String s_key: analyzers.get(key).getCustomParameters().keySet()) {
                settings.put(s_key, (int) gd.getNextNumber());
            }
            analyzers.get(key).setCustomParameters(settings);
        }
    }
    
    
    
}

class Worker extends Thread {
    public boolean stop;
    private App app;
    private ImageGenerator generator;
    private FeedbackController controller;
    private HashMap<String,EvaluationAlgorithm> analyzers;
    private ImagePlus imp;
    
    public Worker(App app, ImageGenerator generator, FeedbackController controller, HashMap<String,EvaluationAlgorithm> analyzers, ImagePlus imp) {
        this.app = app;
        this.generator = generator;
        this.controller = controller;
        this.analyzers = analyzers;
        this.imp = imp;
        stop = false;
    }
    
    @Override
    public void run() {
        ImageProcessor ip;
        while (!stop) {
            app.incrementCounter();
            ip = generator.getNextImage();
            for (EvaluationAlgorithm analyzer: analyzers.values())
                analyzer.processImage(ip.duplicate());
            //System.out.println(image_count);
            controller.adjust();
            
            imp.setSlice(imp.getNSlices());
            imp.updateAndRepaintWindow();
            try {
                sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}