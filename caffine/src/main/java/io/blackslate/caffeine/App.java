package io.blackslate.caffeine;


public class App 
{
    public static void main( String[] args )
    {
    	TestCache testCache = new TestCache();
    	
    	testCache.testCache();
    	
    	testCache.testCacheLoader();
    }
}
