import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.CacheService;
import com.tangosol.net.Cluster;
import com.tangosol.net.NamedCache;
import com.tangosol.net.Service;

public class CoherenceCacheManager  extends AbstractCacheManager{


	public void setCacheManager(com.tangosol.net.CacheFactory cacheFactory) {
		//this.cacheFactory = cacheFactory;
	}

	@Override
	protected Collection<Cache> loadCaches() {
		// TODO Auto-generated method stub

			Collection<Cache> caches = new LinkedHashSet<Cache>();
			Cluster cluster=CacheFactory.ensureCluster();
			System.out.println(">>>>>>>>>>> Cluster Svc Names: " + cluster.getServiceNames().hasMoreElements());
	        for (Enumeration<String> services = cluster.getServiceNames(); services.hasMoreElements(); )
	        {
	             String sName = (String) services.nextElement();
	             System.out.println("Service:"+sName);
	             Service service=cluster.getService(sName);
	             System.out.println("Service:"+sName + " instanceOf " + (service instanceof CacheService));
	             if (service instanceof CacheService){
	                  CacheService cService=(CacheService)service;
	                  Enumeration<String> cohCaches = cService.getCacheNames();
	                  for (cohCaches = cService.getCacheNames(); cohCaches.hasMoreElements();){
	                	   String cacheName = cohCaches.nextElement();
	                       System.out.println("***** Cache:" + cacheName);
	                       caches.add(new CoherenceCache(CacheFactory.getCache (cacheName)));
	                  }
	             }
	        }

		return caches;
	}


	@Override
	public Cache getCache(String name) {
		Cache cache = super.getCache(name);
		if (cache == null) {
			// check the EhCache cache again
			// (in case the cache was added at runtime)
			CoherenceCache cohCache = new CoherenceCache(CacheFactory.getCache(name));
			if (cohCache != null) {
				cache =  cohCache;
				addCache(cache);
			}
		}
		return cache;
	}

}
