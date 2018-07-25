/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.natansevero.cliente;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author natan
 */
public class Worker implements Runnable {
    
    private int n1;
    private int n2;
    private char operation;
    
    final Random random = new Random();
    final char[] operations = {'-', '+'};

    
    @Override
    public void run() {
        
        int n1 = random.nextInt(10);
        int n2 = random.nextInt(10);
        char operation = operations[random.nextInt(2)];
        
        UtilCliente utilCliente = new UtilCliente();
        
        try(DatagramSocket socket = new DatagramSocket()) {
            
            while(true) {
                // init para monitoramento das 100 re   /s
                long init = System.nanoTime();
                    
                utilCliente.send(n1, n2, operation, socket);
              
                int result = utilCliente.receive(socket);
                
                if(processor(n1, n2, operation) == result) System.out.println("Request result: " + result);
                
                // Pegando tempo final da current request
                long endTime = System.nanoTime();
                
                long totalTimePerRequest = endTime - init;
                
                // Mostrando quanto tempo a requisiçao atual durou
                System.out.println("Time current request: " + totalTimePerRequest);
                
                n1 = result;
                n2 = random.nextInt(10);
                operation = operations[random.nextInt(2)];
                
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
        } catch (SocketException ex) {
            Logger.getLogger(ClienteMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int processor(int n1, int n2, char operation) {
                    
        int result = 0;
            
        switch(operation) {
            case '+':
                result = n1 + n2;
            break;
                
            case '-':
                result = n1 - n2;
            break;
        }
        
        return result;
    }
    
}
