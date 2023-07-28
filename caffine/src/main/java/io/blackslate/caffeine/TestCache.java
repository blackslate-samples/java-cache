package io.blackslate.caffeine;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

public class TestCache {
	
	Cache<String, User> userCache;
	
	LoadingCache<String, User> userLoadingCache;
	
	class Listener implements RemovalListener<String, User> {

		@Override
		public void onRemoval(String key, User value, RemovalCause cause) {
			
			System.out.printf("Key %s was evicted (%s)%n", key, cause);
		}
		
	}
	
	public TestCache() {
		
		userCache = Caffeine.newBuilder()
							  .expireAfterWrite(1, TimeUnit.HOURS)
							  .maximumSize(100) 
							  .evictionListener(new Listener())
							  .removalListener(new Listener())
							  .recordStats()
							  .build();
		
		userLoadingCache = Caffeine.newBuilder()
								  .expireAfterWrite(1, TimeUnit.HOURS)
								  .maximumSize(100) 
								  .evictionListener(new Listener())
								  .removalListener(new Listener())
								  .recordStats()
								  .build(key -> UserRepo.getUser(key));
		
		/*
		// Below code uses lambda
		userLoadingCache = Caffeine.newBuilder()
									  .expireAfterWrite(1, TimeUnit.HOURS)
									  .maximumSize(100) 
									  .evictionListener((String key, User user, RemovalCause cause) ->
									  		System.out.printf("Key %s was evicted (%s)%n", key, cause))
									  .removalListener((String key, User user, RemovalCause cause) ->
								        	System.out.printf("Key %s was removed (%s)%n", key, cause))
									  .recordStats()
									  .build(key -> UserRepo.getUser(key));
		*/							  
				
	}
	
	
	public void testCache() {
		
		//Add users to cache
		userCache.put("user_1", new User("user_1", "Rahul", "ADMIN"));
		userCache.put("user_2", new User("user_2", "Kishore", "USER"));
		userCache.put("user_3", new User("user_3", "Ben", "MANAGER"));
		
		//Get user from cache
		User user = userCache.getIfPresent("user_1");
		if (user != null) {
			System.out.println(user.getName());
		}
		
		//Delete user from cache.
		userCache.invalidate("user_1");
		
		//Get stats 
		CacheStats stats = userCache.stats();
		System.out.println(stats.toString());
		
	}
	
	public void testCacheLoader() {
		
		//Get user from cache. 
		//If not available it will be fetched from DB.
		User user = userLoadingCache.get("user_1");
		if (user != null) {
			System.out.println(user.getName());
		}
		
		//Delete user from cache.
		userLoadingCache.invalidate("user_1");
		
		//Get stats 
		CacheStats stats = userCache.stats();
		System.out.println(stats.toString());
		
	}

}
