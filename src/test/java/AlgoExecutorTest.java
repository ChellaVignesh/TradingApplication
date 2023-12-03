import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.example.Algo;
import org.example.signal.AlgoExecutor;
import org.example.signal.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.*;

class AlgoExecutorTest {
  private Algo algo;
  private AlgoExecutor algoExecutor;

  @BeforeEach
  void setUp() {
    algo = mock(Algo.class);
    algoExecutor = new AlgoExecutor(algo);
  }

  @Test
  void getProcessingStatements_ValidProcessingLogic_ReturnsStatements() {
    // Execution
    List<Statement> statements = algoExecutor.getProcessingStatements("setUp();setAlgoParam(1,60);");

    // Verification
    assert statements.size() == 2;
    assert statements.get(0).getMethod().getName().equals("setUp");
    assert statements.get(0).getArgs() == null;

    assert statements.get(1).getMethod().getName().equals("setAlgoParam");
    assert Arrays.equals(statements.get(1).getArgs(), new Object[]{1, 60});
  }

  @Test
  void executeStatements_ValidStatements_MethodsExecuted()
      throws InvocationTargetException, IllegalAccessException {
    // Mocking
    Statement statement1 = mock(Statement.class);
    Statement statement2 = mock(Statement.class);
    Method method1 = mock(Method.class);
    Method method2 = mock(Method.class);
    when(statement1.getMethod()).thenReturn(method1);
    when(statement2.getMethod()).thenReturn(method2);
    List<Statement> statements = List.of(statement1, statement2);

    // Execution
    algoExecutor.executeStatements(statements);

    // Verification
    verify(method1, times(1)).invoke(algo, statement1.getArgs());
    verify(method2, times(1)).invoke(algo, statement2.getArgs());
  }

  private List<Statement> createMockStatements() throws NoSuchMethodException {
    List<Statement> statements = List.of(
        new Statement(getMethod("setUp"), new Object[]{}),
        new Statement(getMethod("reverse"), new Object[]{})
    );
    return statements;
  }

  private Method getMethod(String methodName) throws NoSuchMethodException {
    return Algo.class.getMethod(methodName);
  }
}
