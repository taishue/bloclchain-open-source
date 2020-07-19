package com.coinsthai.startup;

/**
 * Created by
 */
public interface StartupInitializer {
    
    /**
     * 检查是否需要执行初始化
     * 
     * @return
     */
    boolean accept();
    
    /**
     * 执行初始化操作
     */
    void initialize();
}
