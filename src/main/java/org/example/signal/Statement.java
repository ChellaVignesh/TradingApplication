package org.example.signal;

import java.lang.reflect.Method;

public class Statement {

  private final Method method;
  private final Object[] args;


  public Statement(final Method method, final Object[] args) {
    this.method = method;
    this.args = args;
  }

  public Method getMethod() {
    return method;
  }

  public Object[] getArgs() {
    return args;
  }
}
