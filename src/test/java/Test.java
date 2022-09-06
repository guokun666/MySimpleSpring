import com.kun.config.KunAppConfig;
import com.kun.service.GuoService;
import com.kun.service.KunService;
import com.spring.KunApplicationContext;

/**
 * @author guokun
 * @date 2022/9/5 17:05
 */
public class Test {
    public static void main(String[] args) {
        KunApplicationContext kunApplicationContext = new KunApplicationContext(KunAppConfig.class);
        KunService kunService = (KunService) kunApplicationContext.getBean("kunService");
        KunService kunService2 = (KunService) kunApplicationContext.getBean("kunService");
        GuoService guoService = (GuoService) kunApplicationContext.getBean("guoService");
        GuoService guoService2 = (GuoService) kunApplicationContext.getBean("guoService");
        System.out.println(kunService);
        System.out.println(kunService2);
        System.out.println(guoService);
        System.out.println(guoService2);
    }
}
