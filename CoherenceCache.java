import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.tangosol.net.NamedCache;

public class CoherenceCache implements Cache {

	private final NamedCache cache;


	public CoherenceCache(NamedCache _cache)
	{
		cache=_cache;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.cache.clear();
	}

	@Override
	public void evict(Object key) {
		// TODO Auto-generated method stub
		this.cache.remove(key);
	}

	@Override
	public ValueWrapper get(Object key) {
		// TODO Auto-generated method stub
		Object element = this.cache.get(key);
		return (element != null ? new SimpleValueWrapper(element) : null);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.cache.getCacheName();
	}

	@Override
	public Object getNativeCache() {
		// TODO Auto-generated method stub
		return this.cache;
	}

	@Override
	public void put(Object key, Object val) {
		// TODO Auto-generated method stub
		this.cache.put(key,val);
	}


}
