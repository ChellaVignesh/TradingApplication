package org.example;

/**
 * This is a callback interface from our trading system, and we cannot change it.
 * We want to replace the callback with an HTTP API.
 */
public interface SignalHandler {
  void handleSignal(int signal);
}