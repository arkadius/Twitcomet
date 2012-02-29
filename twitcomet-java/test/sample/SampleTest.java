package sample;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

import org.junit.Test;

public class SampleTest {

    @Test
    public void sampleTrue() {
        assertThat(true).isTrue();   
    }
    
    @Test
    public void sampleString() {
    	assertThat("Hello World").containsIgnoringCase("hello").doesNotContain("Hi");
    }
    
}