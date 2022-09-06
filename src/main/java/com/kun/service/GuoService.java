package com.kun.service;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.Scope;

/**
 * @author guokun
 * @date 2022/9/5 17:15
 */
@Component("guoService")
@Scope("prototype")
public class GuoService {
    @Autowired
    private KunService kunService;

    public void test() {
        System.out.println("guoService test()");
    }

    public KunService getKunService() {
        return kunService;
    }
}
