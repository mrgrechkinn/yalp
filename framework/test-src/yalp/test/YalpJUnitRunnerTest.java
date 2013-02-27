package yalp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.JUnit4;

public class YalpJUnitRunnerTest {

	@Test
	public void testFilter() throws Exception {		
		YalpJUnitRunner runner = mock(YalpJUnitRunner.class);
		runner.jUnit4 = new JUnit4(YalpJUnitRunnerTest.class);
		doCallRealMethod().when(runner).filter((Filter) any());

		runner.filter(new Filter() {
			
			@Override
			public boolean shouldRun(Description arg0) {				
				return arg0.getMethodName().indexOf("testFilter") > -1;
			}
			
			@Override
			public String describe() {
				return "";
			}
		});
		
		when(runner.testCount()).thenCallRealMethod();
		when(runner.getDescription()).thenCallRealMethod();
		assertEquals(1, runner.testCount());
	}
	
}
