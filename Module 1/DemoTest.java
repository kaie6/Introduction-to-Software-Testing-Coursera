import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class DemoTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }
    
    private void runMainWithInput(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        outContent.reset();
        Demo.main(new String[]{});
    }

    private void assertMainOutput(boolean expectedTriangle) {
        String output = outContent.toString();
        if (expectedTriangle) {
            assertTrue("Expected a valid triangle, but output was: " + output, output.contains("This is a triangle."));
        } else {
            assertTrue("Expected an invalid triangle, but output was: " + output, output.contains("This is not a triangle."));
        }
    }
    
    // Valid Triangle Test Cases - Main Method
    @Test
    public void testMainMethod_ValidTriangles() {
        String[] validInputs = {"3\n4\n5\n", "5\n5\n5\n", "5\n5\n7\n", "7\n5\n9\n"};
        for (String input : validInputs) {
            runMainWithInput(input);
            assertMainOutput(true);
        }
    }
    
    @Test
    public void testMainMethod_InvalidTriangles() {
        String[] invalidInputs = {"2\n3\n6\n", "2\n6\n3\n", "6\n2\n3\n", "0\n5\n5\n", "-1\n5\n5\n"};
        for (String input : invalidInputs) {
            runMainWithInput(input);
            assertMainOutput(false);
        }
    }
    

    // Valid isTriangle Method Test Cases - Triangle Inequality Rule
    @Test
    public void testIsTriangle_TriangleInequalityRule() {
        // Test cases where all triangle inequality rules are satisfied
        assertTrue("Triangle satisfying all inequality rules should be valid",
            Demo.isTriangle(5, 6, 7));
        
        // Verify each inequality individually
        double a = 5, b = 6, c = 7;
        assertTrue("a + b > c rule should be satisfied", (a + b) > c);
        assertTrue("a + c > b rule should be satisfied", (a + c) > b);
        assertTrue("b + c > a rule should be satisfied", (b + c) > a);

        // Test near-boundary case where inequalities are barely satisfied
        assertTrue("Triangle with barely satisfied inequalities should be valid",
            Demo.isTriangle(10, 10, 19.9));
    }
    
    
    // Valid isTriangle Method Test Cases - testing different valid triangles
    @Test
    public void testIsTriangle_ValidTriangles() {
        // Right triangle
        assertTrue("Right triangle (3,4,5) should be valid", 
            Demo.isTriangle(3, 4, 5));

        // Equilateral triangle
        assertTrue("Equilateral triangle (5,5,5) should be valid", 
            Demo.isTriangle(5, 5, 5));

        // Isosceles triangle
        assertTrue("Isosceles triangle (5,5,7) should be valid", 
            Demo.isTriangle(5, 5, 7));

        // Scalene triangle
        assertTrue("Scalene triangle (7,5,9) should be valid", 
            Demo.isTriangle(7, 5, 9));
    }
    
    // Invalid Triangle Test Cases - isTriangle Method
    @Test
    public void testIsTriangle_InequalityViolations() {
        // Testing a + b = c
        assertFalse("Triangle with a + b = c should be invalid", 
            Demo.isTriangle(3, 3, 6));

        // Testing a + c = b
        assertFalse("Triangle with a + c = b should be invalid", 
            Demo.isTriangle(3, 6, 3));

        // Testing b + c = a
        assertFalse("Triangle with b + c = a should be invalid", 
            Demo.isTriangle(6, 3, 3));
        
        // Testing a + b < c
        assertFalse("Triangle with a + b = c should be invalid", 
            Demo.isTriangle(2, 3, 6));

        // Testing a + c < b
        assertFalse("Triangle with a + c = b should be invalid", 
            Demo.isTriangle(2, 6, 3));

        // Testing b + c < a
        assertFalse("Triangle with b + c = a should be invalid", 
            Demo.isTriangle(6, 2, 3));
    }

    @Test
    public void testIsTriangle_ExtremeSideDifferences() {
        // Test with very large differences between sides
        assertFalse("Triangle with extreme side difference should be invalid", 
            Demo.isTriangle(1000000, 1, 1));
        assertFalse("Triangle with extreme side difference should be invalid", 
            Demo.isTriangle(1, 1000000, 1));
        assertFalse("Triangle with extreme side difference should be invalid", 
            Demo.isTriangle(1, 1, 1000000));

        // Test with valid but very different side lengths
        assertTrue("Triangle with large but valid side differences should be valid", 
            Demo.isTriangle(100, 99, 2));
        assertTrue("Triangle with large but valid side differences should be valid", 
            Demo.isTriangle(500, 498, 3));
    }
    
    @Test
    public void testIsTriangle_FloatingPointPrecision() {
        // Test with floating point values that are close to boundary conditions
        assertTrue("Triangle with small decimal sides should be valid", 
            Demo.isTriangle(0.1, 0.1, 0.1));
            
        // Test with precise decimal calculations using delta
        double sqrt2 = Math.sqrt(2);
        assertTrue("Triangle with precise decimal calculation should be valid", 
            Demo.isTriangle(sqrt2, sqrt2, 2.0));
        
        // Test with very small differences
        assertTrue("Triangle with small differences should be valid", 
            Demo.isTriangle(1.0, 1.0, 1.000001));
        assertFalse("Triangle with sum exactly equal should be invalid", 
            Demo.isTriangle(1.0, 1.0, 2.0));
    }
    
    @Test
    public void testIsTriangle_ZeroOrNegativeSides() {
        assertFalse("Triangle with zero side length should be invalid", 
            Demo.isTriangle(0, 5, 5));
        assertFalse("Triangle with negative side length should be invalid", 
            Demo.isTriangle(-1, 5, 5));
        assertFalse("Triangle with all zero sides should be invalid", 
            Demo.isTriangle(0, 0, 0));
        assertFalse("Triangle with all negative sides should be invalid", 
            Demo.isTriangle(-1, -1, -1));
    }
    
}