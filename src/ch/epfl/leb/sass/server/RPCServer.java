/*
 * Copyright (C) 2017-2018 Laboratory of Experimental Biophysics
 * Ecole Polytechnique Fédérale de Lausanne
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
package ch.epfl.leb.sass.server;

import ch.epfl.leb.sass.ijplugin.Model;
import ch.epfl.leb.sass.simulator.internal.RPCSimulator;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.io.FileInputStream;

/**
 * An RPC server for remote control of the simulation over a network socket.
 * 
 * @author Kyle M. Douglass
 */
public class RPCServer {
    
    public static RemoteSimulationServiceHandler handler;
    public static RemoteSimulationService.Processor processor;
    
    /**
     * The server implementation that is wrapped by this class.
     */
    private TServer server;
       
    /**
     * Creates a new RPCServer and initializes--but does not start--it.
     * 
     * @param model A model of a microscope to simulate.
     * @param port The port number for server communications.
     */
    public RPCServer(Model model, int port) {
        try { 
            RPCSimulator simulator = new RPCSimulator( model.build() );
            handler = new RemoteSimulationServiceHandler(simulator);
            processor = new RemoteSimulationService.Processor(handler);

            TServerTransport serverTransport = new TServerSocket(port);
            server = new TSimpleServer(new Args(serverTransport).processor(processor));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Starts the server.
     */
    public void serve() {
        server.serve();
    }
    
    /**
     * Stops the server.
     */
    public void stop() throws InterruptedException {
        System.out.println("Stopping the server...");
        server.stop();
        
        while ( server.isServing() ) {
            Thread.sleep(500);
        }
        System.out.println("Server stopped.");
    }
    
    /**
     * Checks the status of the server.
     * 
     * @return Is the server running? (true or false)
     */
    public boolean isServing() {
        return server.isServing();
    }
    
    /**
     * Main function used for testing the RPC server.
     * 
     * @param args 
     */
    public static void main(String [] args) {
        try {
            
            // Testing: Load a model from file and create a simulation engine
            Model model;
            model = Model.read(new FileInputStream("/home/douglass/ownCloud/workspace/simulation.sass"));
            
            RPCSimulator simulator = new RPCSimulator( model.build() );
            
            handler = new RemoteSimulationServiceHandler(simulator);
            processor = new RemoteSimulationService.Processor(handler);
            
            Runnable simple = new Runnable() {
                public void run() {
                    simple(processor);
                }
            };
            
            new Thread(simple).start();
        } catch (Exception ex) {
            ex.printStackTrace();;
        }
    }
    
    public static void simple(RemoteSimulationService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
            
            System.out.println("Starting the SASS Simulation Server...");
            server.serve();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
