package org.example.signal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.example.Algo;

public class AlgoExecutor {

  private final Algo algo;

  public AlgoExecutor(Algo algo) {
    this.algo = algo;
  }

  public List<Statement> getProcessingStatements(String processingLogic) {
    List<Statement> processingLogics = new ArrayList<>();
    String[] statements = processingLogic.split(";");
    for (String statement : statements) {
      processingLogics.add(getStatement(statement.trim()));
    }
    return processingLogics;
  }

  public void executeStatements(List<Statement> statements) {
    try {
      for (Statement statement : statements) {
        statement.getMethod().invoke(algo, statement.getArgs());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private Statement getStatement(String statement) {
    // Split the statement into method name and parameters
    String[] parts = statement.split("\\(");
    String methodName = parts[0];
    String[] parameters = parts[1].replace(")", "").split(",");

    // Get the method from the Algo class
    Method method;
    Object[] args = null;
    try {
      // For simplicity, Assuming the library has methods with 2 int params.
      // If more parameterized methods are introduced, then this logic has to further modified.
      Class<?>[] classTypesOfParameters = {int.class, int.class};
      if (StringUtils.isNotBlank(parameters[0])) {
        method = findMethodWithParameters(methodName, classTypesOfParameters);
        args = convertParameters(parameters);
      } else {
        method = findMethod(methodName);
      }
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("No such method found in Algo library ", e);
    }

    return new Statement(method, args);
  }

  Method findMethod(String methodName) throws NoSuchMethodException {
    return Algo.class.getMethod(methodName);
  }

  private Method findMethodWithParameters(String methodName, Class<?>[] classTypesOfParameters) throws NoSuchMethodException {
    return Algo.class.getMethod(methodName, classTypesOfParameters);
  }

  private Object[] convertParameters(String[] parameters) {
    // For simplicity, assuming all parameters are integers
    Object[] args = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      args[i] = Integer.parseInt(parameters[i].trim());
    }
    return args;
  }
}
