package com.github.nds842.sqlfirst.apc;

/**
 * Marker interface for dao classes
 */
public interface SqlSourceDao {
    
    /**
     * @return real class of proxied object
     */
    Class<? extends SqlSourceDao> getUnproxiedClass();
    
}
