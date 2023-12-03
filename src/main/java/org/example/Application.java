package org.example;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.example.signal.AlgoExecutor;
import org.example.signal.Statement;

/**
 * This is your team’s code and should be changed as you see fit.
 */
public class Application implements SignalHandler {

  private AlgoExecutor algoExecutor;

  private final Map<Integer, List<Statement>> signalProcessorMap;

  public Application(AlgoExecutor algoExecutor) {
    algoExecutor = this.algoExecutor;
    signalProcessorMap = loadSignalProcessors();
  }

  private Map<Integer, List<Statement>> loadSignalProcessors() {
    Properties properties = new Properties();

    try (InputStream input = getClass().getClassLoader().getResourceAsStream("signals.properties")) {
      if (input == null) {
        System.out.println("Sorry, unable to find signals.properties");
        throw new RuntimeException("Couldn't load properties file");
      }

      properties.load(input);

      Map<Integer, List<Statement>> signalMap = new HashMap<>();
      for (String key : properties.stringPropertyNames()) {
        int signal = Integer.parseInt(key.replace("signal.", ""));
        String processingLogic = properties.getProperty(key);
        signalMap.put(signal, algoExecutor.getProcessingStatements(processingLogic));
      }

      return signalMap;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  public void handleSignal(int signal) {
    if (signalProcessorMap.containsKey(signal)) {
      algoExecutor.executeStatements(signalProcessorMap.get(signal));
    } else {
      // Execute Cancel Trade
      algoExecutor.executeStatements(signalProcessorMap.get(-1));
    }
    // Execute doAlgo
    algoExecutor.executeStatements(signalProcessorMap.get(0));
  }
}
